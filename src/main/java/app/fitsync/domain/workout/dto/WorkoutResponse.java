package app.fitsync.domain.workout.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "운동 기록 생성 응답")
public record WorkoutResponse(
        @Schema(description = "운동 기록 ID", example = "1")
        long id
) {
}
