package app.fitsync.domain.exercise.dto.exercise;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "운동 생성/수정/삭제 응답")
public record ExerciseResponse(
        @Schema(description = "운동 ID", example = "1")
        Long id
) {
}
