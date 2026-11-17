package bj.lapins.suivi.controller;

import bj.lapins.suivi.service.ParametreService;
import bj.lapins.suivi.service.UtilisateurService;
import java.security.Principal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/parametres")
public class ParametreController {

    private final ParametreService parametreService;
    private final UtilisateurService utilisateurService;

    public ParametreController(ParametreService parametreService,
                               UtilisateurService utilisateurService) {
        this.parametreService = parametreService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public String afficherParametres(Principal principal, Model model) {
        model.addAttribute("parametres", parametreService.obtenirParametres());
        model.addAttribute("utilisateur", utilisateurService.trouverParEmail(principal.getName()));
        return "parametres/index";
    }

    @PostMapping("/exploitation")
    public String modifierExploitation(
            @RequestParam String nomExploitation,
            @RequestParam String commune,
            @RequestParam(required = false) String contact,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        boolean estAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (!estAdmin) {
            throw new AccessDeniedException("Seul un administrateur peut modifier l'exploitation.");
        }

        try {
            parametreService.mettreAJour(nomExploitation, commune, contact);
            redirectAttributes.addFlashAttribute(
                    "succes", "Les informations de l'exploitation ont été mises à jour.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/parametres";
    }

    @PostMapping("/mot-de-passe")
    public String modifierPropreMotDePasse(
            @RequestParam String ancienMotDePasse,
            @RequestParam String nouveauMotDePasse,
            @RequestParam String confirmationMotDePasse,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.modifierPropreMotDePasse(
                    principal.getName(),
                    ancienMotDePasse,
                    nouveauMotDePasse,
                    confirmationMotDePasse);
            redirectAttributes.addFlashAttribute("succes", "Votre mot de passe a été modifié.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/parametres";
    }
}
