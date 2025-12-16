package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.global.util.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

public class ExerciseSpec {
    public static Specification<Exercise> searchWith(
            ExerciseCategory category,
            boolean hidden
    ) {
        return SpecificationBuilder.<Exercise>builder()
                .andEqual("category", category)
                .andEqual("hidden", hidden)
                .build();
    }
}
