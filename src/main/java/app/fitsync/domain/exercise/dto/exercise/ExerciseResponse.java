package app.fitsync.domain.exercise.dto.exercise;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Exercise create/update/delete response")
public record ExerciseResponse(
        @Schema(description = "Exercise ID", example = "1")
        Long id
) {
}
