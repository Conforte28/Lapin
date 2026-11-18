package bj.lapins.suivi.service;

import bj.lapins.suivi.entity.ParametreExploitation;
import bj.lapins.suivi.repository.ParametreExploitationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParametreService {

    private final ParametreExploitationRepository parametreRepository;

    public ParametreService(ParametreExploitationRepository parametreRepository) {
        this.parametreRepository = parametreRepository;
    }

    @Transactional
    public ParametreExploitation obtenirParametres() {
        return parametreRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> parametreRepository.save(
                        new ParametreExploitation("AZWI", "", "")));
    }

    @Transactional
    public void mettreAJour(String nomExploitation, String commune, String contact) {
        String nom = nomExploitation == null ? "" : nomExploitation.trim();
        String communeNettoyee = commune == null ? "" : commune.trim();
        String contactNettoye = contact == null ? "" : contact.trim();

        if (nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'exploitation est obligatoire.");
        }

        ParametreExploitation parametres = obtenirParametres();
        parametres.setNomExploitation(nom);
        parametres.setCommune(communeNettoyee);
        parametres.setContact(contactNettoye);
        parametreRepository.save(parametres);
    }
}
