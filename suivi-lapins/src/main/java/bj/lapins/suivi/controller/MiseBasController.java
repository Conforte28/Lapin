package bj.lapins.suivi.controller;

import bj.lapins.suivi.entity.MiseBas;
import bj.lapins.suivi.service.LapinService;
import bj.lapins.suivi.service.MiseBasService;
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
@RequestMapping("/reproduction")
public class MiseBasController {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");

    private final MiseBasService miseBasService;
    private final LapinService lapinService;

    public MiseBasController(MiseBasService miseBasService, LapinService lapinService) {
        this.miseBasService = miseBasService;
        this.lapinService = lapinService;
    }

    @GetMapping
    public String afficherReproduction(Model model) {
        model.addAttribute("misesBas", miseBasService.listerTout());
        model.addAttribute("femelles", lapinService.listerFemellesVivantes());
        model.addAttribute("males", lapinService.listerMalesVivants());
        model.addAttribute("aujourdHui", LocalDate.now(ZONE_BENIN));
        return "reproduction/liste";
    }

    @PostMapping
    public String enregistrerMiseBas(
            @RequestParam(required = false) Long mereId,
            @RequestParam(required = false) Long pereId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateMiseBas,
            @RequestParam(defaultValue = "0") Integer nombreNesVivants,
            @RequestParam(defaultValue = "0") Integer nombreMortNes,
            @RequestParam(required = false) String observation,
            RedirectAttributes redirectAttributes) {
        try {
            MiseBas miseBas = miseBasService.enregistrer(
                    mereId, pereId, dateMiseBas, nombreNesVivants, nombreMortNes, observation);
            redirectAttributes.addFlashAttribute(
                    "succes",
                    "Mise bas enregistrée. " + miseBas.getNombreNesVivants()
                            + " fiche(s) de lapereau(x) créée(s) automatiquement.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/reproduction";
    }
}
