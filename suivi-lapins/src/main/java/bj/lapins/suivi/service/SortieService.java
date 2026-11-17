package bj.lapins.suivi.service;

import bj.lapins.suivi.entity.Lapin;
import bj.lapins.suivi.entity.Sortie;
import bj.lapins.suivi.entity.StatutLapin;
import bj.lapins.suivi.entity.TypeSortie;
import bj.lapins.suivi.repository.LapinRepository;
import bj.lapins.suivi.repository.SortieRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SortieService {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");

    private final SortieRepository sortieRepository;
    private final LapinRepository lapinRepository;

    public SortieService(SortieRepository sortieRepository, LapinRepository lapinRepository) {
        this.sortieRepository = sortieRepository;
        this.lapinRepository = lapinRepository;
    }

    public List<Sortie> lister(TypeSortie type) {
        if (type == null) {
            return sortieRepository.findAllByOrderByDateSortieDescIdDesc();
        }
        return sortieRepository.findByTypeOrderByDateSortieDescIdDesc(type);
    }

    @Transactional
    public Sortie enregistrerVente(Long lapinId, LocalDate dateSortie, BigDecimal montant, String acheteur) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant de la vente doit être supérieur à zéro.");
        }
        Lapin lapin = validerEtVerifierLapinSortie(lapinId, dateSortie);

        lapin.setStatut(StatutLapin.VENDU);
        lapinRepository.save(lapin);

        Sortie sortie = new Sortie();
        sortie.setLapin(lapin);
        sortie.setType(TypeSortie.VENTE);
        sortie.setDateSortie(dateSortie);
        sortie.setMontant(montant);
        sortie.setAcheteur(nettoyerOptionnel(acheteur));
        return sortieRepository.save(sortie);
    }

    @Transactional
    public Sortie enregistrerDeces(Long lapinId, LocalDate dateSortie, String observation) {
        Lapin lapin = validerEtVerifierLapinSortie(lapinId, dateSortie);

        lapin.setStatut(StatutLapin.MORT);
        lapinRepository.save(lapin);

        Sortie sortie = new Sortie();
        sortie.setLapin(lapin);
        sortie.setType(TypeSortie.DECES);
        sortie.setDateSortie(dateSortie);
        sortie.setObservation(nettoyerOptionnel(observation));
        return sortieRepository.save(sortie);
    }

    private Lapin validerEtVerifierLapinSortie(Long lapinId, LocalDate dateSortie) {
        if (lapinId == null) {
            throw new IllegalArgumentException("Veuillez sélectionner un lapin.");
        }
        if (dateSortie == null) {
            throw new IllegalArgumentException("La date est obligatoire.");
        }
        if (dateSortie.isAfter(LocalDate.now(ZONE_BENIN))) {
            throw new IllegalArgumentException("La date ne peut pas être dans le futur.");
        }

        Lapin lapin = lapinRepository.findById(lapinId)
                .orElseThrow(() -> new IllegalArgumentException("Lapin introuvable."));

        if (lapin.getStatut() != StatutLapin.VIVANT || sortieRepository.existsByLapinId(lapinId)) {
            throw new IllegalStateException(
                    "Ce lapin n'est plus vivant et a déjà fait l'objet d'une sortie.");
        }

        if (lapin.getDateNaissance() != null && dateSortie.isBefore(lapin.getDateNaissance())) {
            throw new IllegalArgumentException(
                    "La date de sortie ne peut pas être antérieure à la naissance du lapin.");
        }

        return lapin;
    }

    private String nettoyerOptionnel(String valeur) {
        if (valeur == null) {
            return null;
        }
        String nettoye = valeur.trim();
        return nettoye.isEmpty() ? null : nettoye;
    }
}
