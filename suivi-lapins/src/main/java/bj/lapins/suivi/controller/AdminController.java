package bj.lapins.suivi.controller;

import bj.lapins.suivi.entity.Role;
import bj.lapins.suivi.service.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/utilisateurs")
public class AdminController {

    private final UtilisateurService utilisateurService;

    public AdminController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public String listerUtilisateurs(Model model) {
        model.addAttribute("utilisateurs", utilisateurService.listerTout());
        model.addAttribute("roles", Role.values());
        return "admin/utilisateurs";
    }

    @PostMapping
    public String creerUtilisateur(
            @RequestParam String email,
            @RequestParam String nomComplet,
            @RequestParam String motDePasse,
            @RequestParam Role role,
            RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.creerUtilisateur(email, nomComplet, motDePasse, role);
            redirectAttributes.addFlashAttribute("succes", "Compte utilisateur créé avec succès.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/admin/utilisateurs";
    }

    @PostMapping("/{id}/toggle-actif")
    public String basculerActif(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.basculerActif(id);
            redirectAttributes.addFlashAttribute("succes", "Statut du compte mis à jour.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/admin/utilisateurs";
    }

    @PostMapping("/{id}/mot-de-passe")
    public String modifierMotDePasse(
            @PathVariable Long id,
            @RequestParam String nouveauMotDePasse,
            RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.modifierMotDePasseParAdmin(id, nouveauMotDePasse);
            redirectAttributes.addFlashAttribute("succes", "Mot de passe modifié avec succès.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        }
        return "redirect:/admin/utilisateurs";
    }
}
