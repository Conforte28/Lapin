package bj.lapins.suivi.repository;

import bj.lapins.suivi.entity.MiseBas;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MiseBasRepository extends JpaRepository<MiseBas, Long> {

    List<MiseBas> findAllByOrderByDateMiseBasDescIdDesc();

    boolean existsByMereIdOrPereId(Long mereId, Long pereId);

    @Query("SELECT COALESCE(SUM(m.nombreNesVivants), 0) FROM MiseBas m WHERE m.dateMiseBas BETWEEN :debut AND :fin")
    long sumNesVivantsEntreDates(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
}
