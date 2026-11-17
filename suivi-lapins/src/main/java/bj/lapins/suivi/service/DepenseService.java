package bj.lapins.suivi.service;

import bj.lapins.suivi.entity.CategorieDepense;
import bj.lapins.suivi.entity.Depense;
import bj.lapins.suivi.repository.DepenseRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepenseService {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");

    private final DepenseRepository depenseRepository;

    public DepenseService(DepenseRepository depenseRepository) {
        this.depenseRepository = depenseRepository;
    }

    public List<Depense> listerTout() {
        return depenseRepository.findAllByOrderByDateDepenseDescIdDesc();
    }

    public Depense trouverParId(Long id) {
        return depenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dépense introuvable."));
    }

    @Transactional
    public Depense enregistrer(LocalDate dateDepense, CategorieDepense categorie,
                               BigDecimal montant, String description) {
        valider(dateDepense, categorie, montant);

        Depense depense = new Depense();
        depense.setDateDepense(dateDepense);
        depense.setCategorie(categorie);
        depense.setMontant(montant);
        depense.setDescription(nettoyerOptionnel(description));
        return depenseRepository.save(depense);
    }

    @Transactional
    public Depense modifier(Long id, LocalDate dateDepense, CategorieDepense categorie,
                            BigDecimal montant, String description) {
        valider(dateDepense, categorie, montant);
        Depense depense = trouverParId(id);
        depense.setDateDepense(dateDepense);
        depense.setCategorie(categorie);
        depense.setMontant(montant);
        depense.setDescription(nettoyerOptionnel(description));
        return depenseRepository.save(depense);
    }

    @Transactional
    public void supprimer(Long id) {
        Depense depense = trouverParId(id);
        depenseRepository.delete(depense);
    }

    private void valider(LocalDate dateDepense, CategorieDepense categorie, BigDecimal montant) {
        if (dateDepense == null) {
            throw new IllegalArgumentException("La date de dépense est obligatoire.");
        }
        if (dateDepense.isAfter(LocalDate.now(ZONE_BENIN))) {
            throw new IllegalArgumentException("La date de dépense ne peut pas être dans le futur.");
        }
        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie est obligatoire.");
        }
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
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
