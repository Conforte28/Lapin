package bj.lapins.suivi.service;

import bj.lapins.suivi.entity.Role;
import bj.lapins.suivi.entity.Utilisateur;
import bj.lapins.suivi.repository.UtilisateurRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> listerTout() {
        return utilisateurRepository.findAllByOrderByIdAsc();
    }

    public Utilisateur trouverParEmail(String email) {
        return utilisateurRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
    }

    @Transactional
    public Utilisateur creerUtilisateur(String email, String nomComplet, String motDePasse, Role role) {
        String emailNettoye = email == null ? "" : email.trim().toLowerCase();
        String nomNettoye = nomComplet == null ? "" : nomComplet.trim();

        if (emailNettoye.isEmpty() || !emailNettoye.contains("@")) {
            throw new IllegalArgumentException("Adresse de connexion invalide.");
        }
        if (utilisateurRepository.existsByEmailIgnoreCase(emailNettoye)) {
            throw new IllegalArgumentException("Cette adresse de connexion est déjà utilisée.");
        }
        if (nomNettoye.isEmpty()) {
            throw new IllegalArgumentException("Le nom complet est obligatoire.");
        }
        if (motDePasse == null || motDePasse.length() < 6) {
            throw new IllegalArgumentException(
                    "Le mot de passe doit contenir au moins 6 caractères.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Le rôle est obligatoire.");
        }

        Utilisateur utilisateur = new Utilisateur(
                emailNettoye,
                nomNettoye,
                passwordEncoder.encode(motDePasse),
                role,
                true);
        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void basculerActif(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));

        if (utilisateur.isActif() && utilisateur.getRole() == Role.ADMIN) {
            long nombreAdminsActifs = utilisateurRepository.countByRoleAndActifTrue(Role.ADMIN);
            if (nombreAdminsActifs <= 1) {
                throw new IllegalStateException(
                        "Impossible de désactiver le dernier administrateur actif.");
            }
        }

        utilisateur.setActif(!utilisateur.isActif());
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void modifierMotDePasseParAdmin(Long id, String nouveauMotDePasse) {
        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
            throw new IllegalArgumentException(
                    "Le mot de passe doit contenir au moins 6 caractères.");
        }
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void modifierPropreMotDePasse(String emailConnecte, String ancienMotDePasse,
                                         String nouveauMotDePasse, String confirmationMotDePasse) {
        Utilisateur utilisateur = trouverParEmail(emailConnecte);

        if (ancienMotDePasse == null
                || !passwordEncoder.matches(ancienMotDePasse, utilisateur.getMotDePasse())) {
            throw new IllegalArgumentException("Le mot de passe actuel est incorrect.");
        }
        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
            throw new IllegalArgumentException(
                    "Le nouveau mot de passe doit contenir au moins 6 caractères.");
        }
        if (!nouveauMotDePasse.equals(confirmationMotDePasse)) {
            throw new IllegalArgumentException("La confirmation du mot de passe ne correspond pas.");
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
    }
}
