package app.fitsync.domain.workout.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "운동 기록 생성 요청")
public record WorkoutRequest(
        @Schema(description = "작성자 사용자 ID", example = "1")
        @Positive
        long writerId,

        @Schema(description = "소유자 사용자 ID", example = "1")
        @Positive
        long ownerId,

        @Schema(description = "운동 기록 메모", example = "하체 루틴 완료")
        @Size(max = 1000)
        String memo,

        @Schema(description = "운동 항목 목록")
        @NotNull
        @NotEmpty
        @Valid
        List<WorkoutExerciseRequest> workoutExercises
) {
}
