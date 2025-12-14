package app.fitsync.domain.routine.repository;

import app.fitsync.domain.routine.entity.RoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, Long> {
}
