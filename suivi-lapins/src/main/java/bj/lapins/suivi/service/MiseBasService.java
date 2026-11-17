package bj.lapins.suivi.service;

import bj.lapins.suivi.entity.Lapin;
import bj.lapins.suivi.entity.MiseBas;
import bj.lapins.suivi.entity.Sexe;
import bj.lapins.suivi.entity.StatutLapin;
import bj.lapins.suivi.repository.LapinRepository;
import bj.lapins.suivi.repository.MiseBasRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiseBasService {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");
    private static final DateTimeFormatter FORMAT_CODE = DateTimeFormatter.ofPattern("yyMMdd");

    private final MiseBasRepository miseBasRepository;
    private final LapinRepository lapinRepository;

    public MiseBasService(MiseBasRepository miseBasRepository, LapinRepository lapinRepository) {
        this.miseBasRepository = miseBasRepository;
        this.lapinRepository = lapinRepository;
    }

    public List<MiseBas> listerTout() {
        return miseBasRepository.findAllByOrderByDateMiseBasDescIdDesc();
    }

    @Transactional
    public MiseBas enregistrer(Long mereId, Long pereId, LocalDate dateMiseBas,
                               Integer nombreNesVivants, Integer nombreMortNes, String observation) {
        if (mereId == null) {
            throw new IllegalArgumentException("La lapine mère est obligatoire.");
        }
        Lapin mere = lapinRepository.findById(mereId)
                .orElseThrow(() -> new IllegalArgumentException("Lapine mère introuvable."));
        if (mere.getSexe() != Sexe.FEMELLE) {
            throw new IllegalArgumentException("La mère sélectionnée doit être une femelle.");
        }
        if (mere.getStatut() != StatutLapin.VIVANT) {
            throw new IllegalArgumentException("La lapine mère doit avoir le statut vivant.");
        }

        Lapin pere = null;
        if (pereId != null) {
            if (pereId.equals(mereId)) {
                throw new IllegalArgumentException("Le père et la mère ne peuvent pas être le même lapin.");
            }
            pere = lapinRepository.findById(pereId)
                    .orElseThrow(() -> new IllegalArgumentException("Lapin père introuvable."));
            if (pere.getSexe() != Sexe.MALE) {
                throw new IllegalArgumentException("Le père sélectionné doit être un mâle.");
            }
        }

        if (dateMiseBas == null) {
            throw new IllegalArgumentException("La date de mise bas est obligatoire.");
        }
        if (dateMiseBas.isAfter(LocalDate.now(ZONE_BENIN))) {
            throw new IllegalArgumentException("La date de mise bas ne peut pas être dans le futur.");
        }
        if (mere.getDateNaissance() != null && dateMiseBas.isBefore(mere.getDateNaissance())) {
            throw new IllegalArgumentException(
                    "La date de mise bas ne peut pas être antérieure à la naissance de la mère.");
        }

        int vivants = nombreNesVivants == null ? 0 : nombreNesVivants;
        int mortNes = nombreMortNes == null ? 0 : nombreMortNes;

        if (vivants < 0 || mortNes < 0) {
            throw new IllegalArgumentException("Les nombres de lapereaux ne peuvent pas être négatifs.");
        }
        if (vivants + mortNes == 0) {
            throw new IllegalArgumentException("Une mise bas doit compter au moins un lapereau.");
        }

        MiseBas miseBas = new MiseBas();
        miseBas.setMere(mere);
        miseBas.setPere(pere);
        miseBas.setDateMiseBas(dateMiseBas);
        miseBas.setNombreNesVivants(vivants);
        miseBas.setNombreMortNes(mortNes);
        miseBas.setObservation(nettoyerOptionnel(observation));

        MiseBas miseBasEnregistree = miseBasRepository.save(miseBas);

        String prefixeDate = "LP-" + dateMiseBas.format(FORMAT_CODE) + "-";
        int sequence = 1;
        for (int i = 0; i < vivants; i++) {
            String codeGenere;
            do {
                codeGenere = prefixeDate + String.format("%02d", sequence);
                sequence++;
            } while (lapinRepository.existsByCodeIgnoreCase(codeGenere));

            Lapin lapereau = new Lapin();
            lapereau.setCode(codeGenere);
            lapereau.setSexe(Sexe.INCONNU);
            lapereau.setDateNaissance(dateMiseBas);
            lapereau.setClapier(mere.getClapier());
            lapereau.setRace(mere.getRace());
            lapereau.setStatut(StatutLapin.VIVANT);
            lapereau.setMiseBasOrigine(miseBasEnregistree);
            lapinRepository.save(lapereau);
        }

        return miseBasEnregistree;
    }

    private String nettoyerOptionnel(String valeur) {
        if (valeur == null) {
            return null;
        }
        String nettoye = valeur.trim();
        return nettoye.isEmpty() ? null : nettoye;
    }
}
