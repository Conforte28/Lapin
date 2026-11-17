package bj.lapins.suivi.repository;

import bj.lapins.suivi.entity.Depense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepenseRepository extends JpaRepository<Depense, Long> {

    List<Depense> findAllByOrderByDateDepenseDescIdDesc();

    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Depense d WHERE d.dateDepense BETWEEN :debut AND :fin")
    BigDecimal sumMontantEntreDates(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
}
