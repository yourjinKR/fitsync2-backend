package app.fitsync.domain.routine.dto.routine;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "루틴 생성/수정/삭제 응답")
public record RoutineResponse(
        @Schema(description = "루틴 ID", example = "1")
        long id
) {
}
