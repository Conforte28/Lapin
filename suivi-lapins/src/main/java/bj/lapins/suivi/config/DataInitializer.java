package bj.lapins.suivi.config;

import bj.lapins.suivi.entity.CategorieDepense;
import bj.lapins.suivi.entity.Lapin;
import bj.lapins.suivi.entity.Role;
import bj.lapins.suivi.entity.Sexe;
import bj.lapins.suivi.repository.DepenseRepository;
import bj.lapins.suivi.repository.LapinRepository;
import bj.lapins.suivi.repository.MiseBasRepository;
import bj.lapins.suivi.repository.SortieRepository;
import bj.lapins.suivi.repository.UtilisateurRepository;
import bj.lapins.suivi.service.DepenseService;
import bj.lapins.suivi.service.LapinService;
import bj.lapins.suivi.service.MiseBasService;
import bj.lapins.suivi.service.ParametreService;
import bj.lapins.suivi.service.SortieService;
import bj.lapins.suivi.service.UtilisateurService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");

    private final boolean demoEnabled;
    private final String demoPassword;
    private final UtilisateurRepository utilisateurRepository;
    private final LapinRepository lapinRepository;
    private final MiseBasRepository miseBasRepository;
    private final SortieRepository sortieRepository;
    private final DepenseRepository depenseRepository;
    private final UtilisateurService utilisateurService;
    private final ParametreService parametreService;
    private final LapinService lapinService;
    private final MiseBasService miseBasService;
    private final SortieService sortieService;
    private final DepenseService depenseService;

    public DataInitializer(
            @Value("${app.demo.enabled:false}") boolean demoEnabled,
            @Value("${app.demo.password:}") String demoPassword,
            UtilisateurRepository utilisateurRepository,
            LapinRepository lapinRepository,
            MiseBasRepository miseBasRepository,
            SortieRepository sortieRepository,
            DepenseRepository depenseRepository,
            UtilisateurService utilisateurService,
            ParametreService parametreService,
            LapinService lapinService,
            MiseBasService miseBasService,
            SortieService sortieService,
            DepenseService depenseService) {
        this.demoEnabled = demoEnabled;
        this.demoPassword = demoPassword;
        this.utilisateurRepository = utilisateurRepository;
        this.lapinRepository = lapinRepository;
        this.miseBasRepository = miseBasRepository;
        this.sortieRepository = sortieRepository;
        this.depenseRepository = depenseRepository;
        this.utilisateurService = utilisateurService;
        this.parametreService = parametreService;
        this.lapinService = lapinService;
        this.miseBasService = miseBasService;
        this.sortieService = sortieService;
        this.depenseService = depenseService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        parametreService.obtenirParametres();

        if (!demoEnabled || demoPassword == null || demoPassword.isBlank()) {
            return;
        }

        if (utilisateurRepository.count() == 0) {
            utilisateurService.creerUtilisateur(
                    "admin@lapins.bj",
                    "Administrateur",
                    demoPassword,
                    Role.ADMIN);
            utilisateurService.creerUtilisateur(
                    "agent@lapins.bj",
                    "Agent",
                    demoPassword,
                    Role.AGENT);
        }

        boolean baseVide = lapinRepository.count() == 0
                && miseBasRepository.count() == 0
                && sortieRepository.count() == 0
                && depenseRepository.count() == 0;

        if (baseVide) {
            parametreService.mettreAJour("AZWI", "", "");

            LocalDate aujourdHui = LocalDate.now(ZONE_BENIN);
            LocalDate dateMoisCourant = aujourdHui.getDayOfMonth() > 3
                    ? aujourdHui.minusDays(2)
                    : aujourdHui;

            Lapin mere1 = lapinService.ajouter(
                    "LAP-F01",
                    Sexe.FEMELLE,
                    null,
                    aujourdHui.minusMonths(9),
                    "Clapier A1",
                    new BigDecimal("3.60"));

            lapinService.ajouter(
                    "LAP-F02",
                    Sexe.FEMELLE,
                    null,
                    aujourdHui.minusMonths(7),
                    "Clapier A2",
                    new BigDecimal("3.30"));

            Lapin pere1 = lapinService.ajouter(
                    "LAP-M01",
                    Sexe.MALE,
                    null,
                    aujourdHui.minusMonths(10),
                    "Clapier B1",
                    new BigDecimal("3.90"));

            Lapin lapinAVendre = lapinService.ajouter(
                    "LAP-004",
                    Sexe.MALE,
                    null,
                    aujourdHui.minusMonths(4),
                    "Clapier C1",
                    new BigDecimal("2.45"));

            Lapin lapinDecede = lapinService.ajouter(
                    "LAP-005",
                    Sexe.FEMELLE,
                    null,
                    aujourdHui.minusMonths(3),
                    "Clapier C2",
                    new BigDecimal("1.95"));

            miseBasService.enregistrer(
                    mere1.getId(),
                    pere1.getId(),
                    dateMoisCourant,
                    4,
                    1,
                    null);

            sortieService.enregistrerVente(
                    lapinAVendre.getId(),
                    dateMoisCourant,
                    new BigDecimal("12000"),
                    null);

            sortieService.enregistrerDeces(
                    lapinDecede.getId(),
                    dateMoisCourant,
                    null);

            depenseService.enregistrer(
                    dateMoisCourant,
                    CategorieDepense.ALIMENTATION,
                    new BigDecimal("6500"),
                    "Provende");

            depenseService.enregistrer(
                    dateMoisCourant,
                    CategorieDepense.SOINS,
                    new BigDecimal("2000"),
                    "Soins vétérinaires");

            depenseService.enregistrer(
                    dateMoisCourant,
                    CategorieDepense.ENTRETIEN,
                    new BigDecimal("1500"),
                    "Entretien clapier");
        }
    }
}
