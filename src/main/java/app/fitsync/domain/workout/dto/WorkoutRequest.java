package app.fitsync.domain.workout.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "운동 기록 생성 요청")
public record WorkoutRequest(
        @Schema(description = "작성자 사용자 ID", example = "1")
        long writerId,
        @Schema(description = "소유자 사용자 ID", example = "1")
        long ownerId,
        @Schema(description = "운동 기록 메모", example = "하체 루틴 완료")
        String memo,
        @Schema(description = "운동 항목 목록")
        List<WorkoutExerciseRequest> workoutExercises
) {
}
