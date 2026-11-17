package bj.lapins.suivi.controller;

import bj.lapins.suivi.entity.Lapin;
import bj.lapins.suivi.entity.Sexe;
import bj.lapins.suivi.entity.StatutLapin;
import bj.lapins.suivi.service.LapinService;
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
@RequestMapping("/lapins")
public class LapinController {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");

    private final LapinService lapinService;

    public LapinController(LapinService lapinService) {
        this.lapinService = lapinService;
    }

    @GetMapping
    public String lister(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Sexe sexe,
            @RequestParam(required = false) StatutLapin statut,
            Model model) {
        model.addAttribute("lapins", lapinService.rechercher(code, sexe, statut));
        model.addAttribute("codeFiltre", code == null ? "" : code);
        model.addAttribute("sexeFiltre", sexe);
        model.addAttribute("statutFiltre", statut);
        model.addAttribute("sexes", Sexe.values());
        model.addAttribute("statuts", StatutLapin.values());
        return "lapins/liste";
    }

    @GetMapping("/nouveau")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("lapin", new Lapin());
        model.addAttribute("sexes", Sexe.values());
        model.addAttribute("modeEdition", false);
        model.addAttribute("aujourdHui", LocalDate.now(ZONE_BENIN));
        return "lapins/formulaire";
    }

    @PostMapping
    public String ajouter(
            @RequestParam String code,
            @RequestParam Sexe sexe,
            @RequestParam(required = false) String race,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateNaissance,
            @RequestParam(required = false) String clapier,
            @RequestParam(required = false) BigDecimal poidsKg,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            lapinService.ajouter(code, sexe, race, dateNaissance, clapier, poidsKg);
            redirectAttributes.addFlashAttribute("succes", "Le lapin a été ajouté avec succès.");
            return "redirect:/lapins";
        } catch (RuntimeException ex) {
            Lapin saisie = new Lapin();
            saisie.setCode(code);
            saisie.setSexe(sexe);
            saisie.setRace(race);
            saisie.setDateNaissance(dateNaissance);
            saisie.setClapier(clapier);
            saisie.setPoidsKg(poidsKg);
            saisie.setStatut(StatutLapin.VIVANT);

            model.addAttribute("lapin", saisie);
            model.addAttribute("sexes", Sexe.values());
            model.addAttribute("modeEdition", false);
            model.addAttribute("aujourdHui", LocalDate.now(ZONE_BENIN));
            model.addAttribute("erreur", ex.getMessage());
            return "lapins/formulaire";
        }
    }

    @GetMapping("/{id}/modifier")
    public String afficherFormulaireModification(@PathVariable Long id, Model model,
                                                 RedirectAttributes redirectAttributes) {
        try {
            Lapin lapin = lapinService.trouverParId(id);
            model.addAttribute("lapin", lapin);
            model.addAttribute("sexes", Sexe.values());
            model.addAttribute("modeEdition", true);
            model.addAttribute("aujourdHui", LocalDate.now(ZONE_BENIN));
            return "lapins/formulaire";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
            return "redirect:/lapins";
        }
    }

    @PostMapping("/{id}/modifier")
    public String modifier(
            @PathVariable Long id,
            @RequestParam String code,
            @RequestParam Sexe sexe,
            @RequestParam(required = false) String race,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateNaissance,
            @RequestParam(required = false) String clapier,
            @RequestParam(required = false) BigDecimal poidsKg,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            lapinService.modifier(id, code, sexe, race, dateNaissance, clapier, poidsKg);
            redirectAttributes.addFlashAttribute("succes", "La fiche du lapin a été mise à jour.");
            return "redirect:/lapins";
        } catch (RuntimeException ex) {
            Lapin existant = lapinService.trouverParId(id);
            existant.setCode(code);
            existant.setSexe(sexe);
            existant.setRace(race);
            existant.setDateNaissance(dateNaissance);
            existant.setClapier(clapier);
            existant.setPoidsKg(poidsKg);

            model.addAttribute("lapin", existant);
            model.addAttribute("sexes", Sexe.values());
            model.addAttribute("modeEdition", true);
            model.addAttribute("aujourdHui", LocalDate.now(ZONE_BENIN));
            model.addAttribute("erreur", ex.getMessage());
            return "lapins/formulaire";
        }
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            lapinService.supprimer(id);
            redirectAttributes.addFlashAttribute("succes", "Le lapin a été supprimé.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/lapins";
    }
}
