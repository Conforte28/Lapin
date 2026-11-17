package bj.lapins.suivi.repository;

import bj.lapins.suivi.entity.Lapin;
import bj.lapins.suivi.entity.Sexe;
import bj.lapins.suivi.entity.StatutLapin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LapinRepository extends JpaRepository<Lapin, Long> {

    boolean existsByCodeIgnoreCase(String code);

    Optional<Lapin> findByCodeIgnoreCase(String code);

    long countByStatut(StatutLapin statut);

    List<Lapin> findAllByOrderByIdDesc();

    List<Lapin> findByStatutOrderByCodeAsc(StatutLapin statut);

    List<Lapin> findBySexeOrderByCodeAsc(Sexe sexe);

    List<Lapin> findBySexeAndStatutOrderByCodeAsc(Sexe sexe, StatutLapin statut);
}
