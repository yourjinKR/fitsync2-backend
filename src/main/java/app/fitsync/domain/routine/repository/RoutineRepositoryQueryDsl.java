package app.fitsync.domain.routine.repository;

import app.fitsync.domain.routine.entity.Routine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoutineRepositoryQueryDsl {
    Page<Routine> search(Pageable pageable, Long ownerId, Long writerId);
}
