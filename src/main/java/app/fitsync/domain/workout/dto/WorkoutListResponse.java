package app.fitsync.domain.workout.dto;

import java.time.LocalDateTime;

public record WorkoutListResponse(
        long id,
        LocalDateTime createdAt
) {
}
