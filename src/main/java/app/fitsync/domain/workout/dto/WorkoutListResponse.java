package app.fitsync.domain.workout.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Workout list item")
public record WorkoutListResponse(
        long id,
        LocalDateTime createdAt
) {
}
