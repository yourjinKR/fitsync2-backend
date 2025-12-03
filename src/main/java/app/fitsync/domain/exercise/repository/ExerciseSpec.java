package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import org.springframework.data.jpa.domain.Specification;

public class ExerciseSpec {
    public static Specification<Exercise> category(ExerciseCategory category) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Exercise> hidden(boolean hidden) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("hidden"), hidden);
    }
}
