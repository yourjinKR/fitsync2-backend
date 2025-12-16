package app.fitsync.domain.routine.repository;

import app.fitsync.domain.routine.entity.Routine;
import app.fitsync.global.util.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

public class RoutineSpec {
    public static Specification<Routine> searchWith(
            Long ownerId,
            Long writerId
    ) {
        return SpecificationBuilder.<Routine>builder()
                .andEqual("owner.id", ownerId)
                .andEqual("writer.id", writerId)
                .build();
    }
}
