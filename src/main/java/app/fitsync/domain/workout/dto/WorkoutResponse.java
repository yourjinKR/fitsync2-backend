package app.fitsync.domain.workout.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Workout create response")
public record WorkoutResponse(
        @Schema(description = "Workout ID", example = "1")
        long id
) {
}
