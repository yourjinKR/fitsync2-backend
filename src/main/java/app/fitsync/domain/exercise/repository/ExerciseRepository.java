package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.entity.Exercise;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query("SELECT DISTINCT e " +
            "FROM Exercise e " +
            "LEFT JOIN FETCH e.targets t " +
            "LEFT JOIN FETCH t.bodyDetailPart bd " +
            "LEFT JOIN FETCH bd.bodyPart bp " +
            "LEFT JOIN FETCH e.effects " +
            "WHERE e.id = :id")
    Optional<Exercise> findByIdWithDetails(@Param("id") Long id);
}
