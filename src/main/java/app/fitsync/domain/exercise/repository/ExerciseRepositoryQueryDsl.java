package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExerciseRepositoryQueryDsl {
    Page<Exercise> search(Pageable pageable, ExerciseCategory category, boolean hidden);
}
