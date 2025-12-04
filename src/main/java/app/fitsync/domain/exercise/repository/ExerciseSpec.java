package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
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

    public static Specification<Exercise> searchWith(
            ExerciseCategory category,
            boolean hidden
    ) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            predicates.add(criteriaBuilder.equal(root.get("hidden"), hidden));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
