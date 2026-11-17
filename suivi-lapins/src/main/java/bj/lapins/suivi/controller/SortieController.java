package bj.lapins.suivi.controller;

import bj.lapins.suivi.entity.TypeSortie;
import bj.lapins.suivi.service.LapinService;
import bj.lapins.suivi.service.SortieService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sorties")
public class SortieController {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");

    private final SortieService sortieService;
    private final LapinService lapinService;

    public SortieController(SortieService sortieService, LapinService lapinService) {
        this.sortieService = sortieService;
        this.lapinService = lapinService;
    }

    @GetMapping
    public String afficherSorties(@RequestParam(required = false) TypeSortie type, Model model) {
        model.addAttribute("sorties", sortieService.lister(type));
        model.addAttribute("typeFiltre", type);
        model.addAttribute("typesSortie", TypeSortie.values());
        model.addAttribute("lapinsVivants", lapinService.listerVivants());
        model.addAttribute("aujourdHui", LocalDate.now(ZONE_BENIN));
        return "sorties/liste";
    }

    @PostMapping("/vente")
    public String enregistrerVente(
            @RequestParam(required = false) Long lapinId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateSortie,
            @RequestParam(required = false) BigDecimal montant,
            @RequestParam(required = false) String acheteur,
            RedirectAttributes redirectAttributes) {
        try {
            sortieService.enregistrerVente(lapinId, dateSortie, montant, acheteur);
            redirectAttributes.addFlashAttribute("succes", "Vente enregistrée avec succès.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/sorties";
    }

    @PostMapping("/deces")
    public String enregistrerDeces(
            @RequestParam(required = false) Long lapinId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateSortie,
            @RequestParam(required = false) String observation,
            RedirectAttributes redirectAttributes) {
        try {
            sortieService.enregistrerDeces(lapinId, dateSortie, observation);
            redirectAttributes.addFlashAttribute("succes", "Décès enregistré avec succès.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/sorties";
    }
}
