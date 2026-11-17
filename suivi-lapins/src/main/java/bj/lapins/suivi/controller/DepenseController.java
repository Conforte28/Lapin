package bj.lapins.suivi.controller;

import bj.lapins.suivi.entity.CategorieDepense;
import bj.lapins.suivi.entity.Depense;
import bj.lapins.suivi.service.DepenseService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/depenses")
public class DepenseController {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");

    private final DepenseService depenseService;

    public DepenseController(DepenseService depenseService) {
        this.depenseService = depenseService;
    }

    @GetMapping
    public String lister(Model model) {
        model.addAttribute("depenses", depenseService.listerTout());
        model.addAttribute("categories", CategorieDepense.values());
        model.addAttribute("aujourdHui", LocalDate.now(ZONE_BENIN));
        return "depenses/liste";
    }

    @PostMapping
    public String enregistrer(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateDepense,
            @RequestParam(required = false) CategorieDepense categorie,
            @RequestParam(required = false) BigDecimal montant,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {
        try {
            depenseService.enregistrer(dateDepense, categorie, montant, description);
            redirectAttributes.addFlashAttribute("succes", "Dépense enregistrée avec succès.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/depenses";
    }

    @GetMapping("/{id}/modifier")
    public String afficherModification(@PathVariable Long id, Model model,
                                       RedirectAttributes redirectAttributes) {
        try {
            Depense depense = depenseService.trouverParId(id);
            model.addAttribute("depense", depense);
            model.addAttribute("categories", CategorieDepense.values());
            model.addAttribute("aujourdHui", LocalDate.now(ZONE_BENIN));
            return "depenses/modifier";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
            return "redirect:/depenses";
        }
    }

    @PostMapping("/{id}/modifier")
    public String modifier(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateDepense,
            @RequestParam(required = false) CategorieDepense categorie,
            @RequestParam(required = false) BigDecimal montant,
            @RequestParam(required = false) String description,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            depenseService.modifier(id, dateDepense, categorie, montant, description);
            redirectAttributes.addFlashAttribute("succes", "La dépense a été mise à jour.");
            return "redirect:/depenses";
        } catch (RuntimeException ex) {
            Depense existante = depenseService.trouverParId(id);
            existante.setDateDepense(dateDepense);
            existante.setCategorie(categorie);
            existante.setMontant(montant);
            existante.setDescription(description);

            model.addAttribute("depense", existante);
            model.addAttribute("categories", CategorieDepense.values());
            model.addAttribute("aujourdHui", LocalDate.now(ZONE_BENIN));
            model.addAttribute("erreur", ex.getMessage());
            return "depenses/modifier";
        }
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            depenseService.supprimer(id);
            redirectAttributes.addFlashAttribute("succes", "La dépense a été supprimée.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/depenses";
    }
}
