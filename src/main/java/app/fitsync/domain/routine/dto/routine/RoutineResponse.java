package app.fitsync.domain.routine.dto.routine;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Routine create/update/delete response")
public record RoutineResponse(
        @Schema(description = "Routine ID", example = "1")
        long id
) {
}
