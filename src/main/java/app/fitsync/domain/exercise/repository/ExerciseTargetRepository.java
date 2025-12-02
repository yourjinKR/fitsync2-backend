package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.entity.ExerciseTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseTargetRepository extends JpaRepository<ExerciseTarget, Long> {
}
