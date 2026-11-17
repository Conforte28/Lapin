package bj.lapins.suivi.service;

import bj.lapins.suivi.entity.StatutLapin;
import bj.lapins.suivi.entity.TypeSortie;
import bj.lapins.suivi.repository.DepenseRepository;
import bj.lapins.suivi.repository.LapinRepository;
import bj.lapins.suivi.repository.MiseBasRepository;
import bj.lapins.suivi.repository.SortieRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private static final ZoneId ZONE_BENIN = ZoneId.of("Africa/Porto-Novo");

    private final LapinRepository lapinRepository;
    private final MiseBasRepository miseBasRepository;
    private final SortieRepository sortieRepository;
    private final DepenseRepository depenseRepository;

    public DashboardService(LapinRepository lapinRepository,
                            MiseBasRepository miseBasRepository,
                            SortieRepository sortieRepository,
                            DepenseRepository depenseRepository) {
        this.lapinRepository = lapinRepository;
        this.miseBasRepository = miseBasRepository;
        this.sortieRepository = sortieRepository;
        this.depenseRepository = depenseRepository;
    }

    public IndicateursDashboard calculerIndicateursDuMois() {
        YearMonth moisCourant = YearMonth.now(ZONE_BENIN);
        LocalDate debutMois = moisCourant.atDay(1);
        LocalDate finMois = moisCourant.atEndOfMonth();

        long lapinsVivants = lapinRepository.countByStatut(StatutLapin.VIVANT);
        long nesVivantsMois = miseBasRepository.sumNesVivantsEntreDates(debutMois, finMois);
        long decesMois = sortieRepository.countByTypeAndDateSortieBetween(
                TypeSortie.DECES, debutMois, finMois);

        BigDecimal ventesMois = sortieRepository.sumMontantByTypeEntreDates(
                TypeSortie.VENTE, debutMois, finMois);
        if (ventesMois == null) {
            ventesMois = BigDecimal.ZERO;
        }

        BigDecimal depensesMois = depenseRepository.sumMontantEntreDates(debutMois, finMois);
        if (depensesMois == null) {
            depensesMois = BigDecimal.ZERO;
        }

        BigDecimal soldeMois = ventesMois.subtract(depensesMois);
        String moisLibelle = moisCourant.format(
                DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH));

        return new IndicateursDashboard(
                lapinsVivants,
                nesVivantsMois,
                decesMois,
                ventesMois,
                depensesMois,
                soldeMois,
                moisLibelle);
    }

    public static class IndicateursDashboard {

        private final long lapinsVivants;
        private final long nesVivantsMois;
        private final long decesMois;
        private final BigDecimal ventesMois;
        private final BigDecimal depensesMois;
        private final BigDecimal soldeMois;
        private final String moisLibelle;

        public IndicateursDashboard(long lapinsVivants,
                                    long nesVivantsMois,
                                    long decesMois,
                                    BigDecimal ventesMois,
                                    BigDecimal depensesMois,
                                    BigDecimal soldeMois,
                                    String moisLibelle) {
            this.lapinsVivants = lapinsVivants;
            this.nesVivantsMois = nesVivantsMois;
            this.decesMois = decesMois;
            this.ventesMois = ventesMois;
            this.depensesMois = depensesMois;
            this.soldeMois = soldeMois;
            this.moisLibelle = moisLibelle;
        }

        public long getLapinsVivants() {
            return lapinsVivants;
        }

        public long getNesVivantsMois() {
            return nesVivantsMois;
        }

        public long getDecesMois() {
            return decesMois;
        }

        public BigDecimal getVentesMois() {
            return ventesMois;
        }

        public BigDecimal getDepensesMois() {
            return depensesMois;
        }

        public BigDecimal getSoldeMois() {
            return soldeMois;
        }

        public String getMoisLibelle() {
            return moisLibelle;
        }
    }
}
