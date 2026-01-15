package app.fitsync.domain.workout.repository;

import app.fitsync.domain.workout.entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Page<Workout> findByOwnerId(long ownerId, Pageable pageable);
}
