package bj.lapins.suivi.repository;

import bj.lapins.suivi.entity.Sortie;
import bj.lapins.suivi.entity.TypeSortie;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SortieRepository extends JpaRepository<Sortie, Long> {

    List<Sortie> findAllByOrderByDateSortieDescIdDesc();

    List<Sortie> findByTypeOrderByDateSortieDescIdDesc(TypeSortie type);

    boolean existsByLapinId(Long lapinId);

    Optional<Sortie> findByLapinId(Long lapinId);

    long countByTypeAndDateSortieBetween(TypeSortie type, LocalDate debut, LocalDate fin);

    @Query("SELECT COALESCE(SUM(s.montant), 0) FROM Sortie s WHERE s.type = :type AND s.dateSortie BETWEEN :debut AND :fin")
    BigDecimal sumMontantByTypeEntreDates(@Param("type") TypeSortie type, @Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
}
