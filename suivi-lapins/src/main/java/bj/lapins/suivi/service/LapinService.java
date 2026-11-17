package bj.lapins.suivi.service;

import bj.lapins.suivi.entity.Lapin;
import bj.lapins.suivi.entity.Sexe;
import bj.lapins.suivi.entity.Sortie;
import bj.lapins.suivi.entity.StatutLapin;
import bj.lapins.suivi.repository.LapinRepository;
import bj.lapins.suivi.repository.MiseBasRepository;
import bj.lapins.suivi.repository.SortieRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LapinService {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");

    private final LapinRepository lapinRepository;
    private final MiseBasRepository miseBasRepository;
    private final SortieRepository sortieRepository;

    public LapinService(LapinRepository lapinRepository,
                        MiseBasRepository miseBasRepository,
                        SortieRepository sortieRepository) {
        this.lapinRepository = lapinRepository;
        this.miseBasRepository = miseBasRepository;
        this.sortieRepository = sortieRepository;
    }

    public List<Lapin> rechercher(String code, Sexe sexe, StatutLapin statut) {
        String recherche = code == null ? "" : code.trim().toLowerCase();
        return lapinRepository.findAllByOrderByIdDesc().stream()
                .filter(l -> recherche.isEmpty() || l.getCode().toLowerCase().contains(recherche))
                .filter(l -> sexe == null || l.getSexe() == sexe)
                .filter(l -> statut == null || l.getStatut() == statut)
                .collect(Collectors.toList());
    }

    public Lapin trouverParId(Long id) {
        return lapinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lapin introuvable."));
    }

    public List<Lapin> listerVivants() {
        return lapinRepository.findByStatutOrderByCodeAsc(StatutLapin.VIVANT);
    }

    public List<Lapin> listerFemellesVivantes() {
        return lapinRepository.findBySexeAndStatutOrderByCodeAsc(Sexe.FEMELLE, StatutLapin.VIVANT);
    }

    public List<Lapin> listerMalesVivants() {
        return lapinRepository.findBySexeAndStatutOrderByCodeAsc(Sexe.MALE, StatutLapin.VIVANT);
    }

    @Transactional
    public Lapin ajouter(String code, Sexe sexe, String race, LocalDate dateNaissance,
                         String clapier, BigDecimal poidsKg) {
        String codeNettoye = validerEtNettoyerCode(code);
        if (lapinRepository.existsByCodeIgnoreCase(codeNettoye)) {
            throw new IllegalArgumentException("Ce code de lapin existe déjà.");
        }
        validerChampsCommuns(sexe, dateNaissance, poidsKg);

        Lapin lapin = new Lapin();
        lapin.setCode(codeNettoye);
        lapin.setSexe(sexe);
        lapin.setRace(nettoyerOptionnel(race));
        lapin.setDateNaissance(dateNaissance);
        lapin.setClapier(nettoyerOptionnel(clapier));
        lapin.setPoidsKg(poidsKg);
        lapin.setStatut(StatutLapin.VIVANT);
        return lapinRepository.save(lapin);
    }

    @Transactional
    public Lapin modifier(Long id, String code, Sexe sexe, String race, LocalDate dateNaissance,
                          String clapier, BigDecimal poidsKg) {
        Lapin lapin = trouverParId(id);
        String codeNettoye = validerEtNettoyerCode(code);
        if (!lapin.getCode().equalsIgnoreCase(codeNettoye)
                && lapinRepository.existsByCodeIgnoreCase(codeNettoye)) {
            throw new IllegalArgumentException("Un autre lapin utilise déjà ce code.");
        }
        validerChampsCommuns(sexe, dateNaissance, poidsKg);

        Optional<Sortie> sortieOpt = sortieRepository.findByLapinId(id);
        if (sortieOpt.isPresent() && dateNaissance != null
                && dateNaissance.isAfter(sortieOpt.get().getDateSortie())) {
            throw new IllegalArgumentException(
                    "La date de naissance ne peut pas être après la date de sortie du lapin.");
        }

        lapin.setCode(codeNettoye);
        lapin.setSexe(sexe);
        lapin.setRace(nettoyerOptionnel(race));
        lapin.setDateNaissance(dateNaissance);
        lapin.setClapier(nettoyerOptionnel(clapier));
        lapin.setPoidsKg(poidsKg);
        return lapinRepository.save(lapin);
    }

    @Transactional
    public void supprimer(Long id) {
        Lapin lapin = trouverParId(id);
        boolean lieAMiseBas = miseBasRepository.existsByMereIdOrPereId(id, id)
                || lapin.getMiseBasOrigine() != null;
        boolean lieASortie = sortieRepository.existsByLapinId(id);

        if (lieAMiseBas || lieASortie) {
            throw new IllegalStateException(
                    "Impossible de supprimer ce lapin car il est lié à un historique de reproduction ou de sortie.");
        }
        lapinRepository.delete(lapin);
    }

    private String validerEtNettoyerCode(String code) {
        String nettoye = code == null ? "" : code.trim();
        if (nettoye.isEmpty()) {
            throw new IllegalArgumentException("Le code du lapin est obligatoire.");
        }
        return nettoye;
    }

    private void validerChampsCommuns(Sexe sexe, LocalDate dateNaissance, BigDecimal poidsKg) {
        if (sexe == null) {
            throw new IllegalArgumentException("Le sexe du lapin est obligatoire.");
        }
        if (dateNaissance != null && dateNaissance.isAfter(LocalDate.now(ZONE_BENIN))) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur.");
        }
        if (poidsKg != null && poidsKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le poids doit être supérieur à zéro.");
        }
    }

    private String nettoyerOptionnel(String valeur) {
        if (valeur == null) {
            return null;
        }
        String nettoye = valeur.trim();
        return nettoye.isEmpty() ? null : nettoye;
    }
}
