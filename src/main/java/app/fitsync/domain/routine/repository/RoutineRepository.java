package app.fitsync.domain.routine.repository;

import app.fitsync.domain.routine.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long>, JpaSpecificationExecutor<Routine>, RoutineRepositoryQueryDsl {

}
