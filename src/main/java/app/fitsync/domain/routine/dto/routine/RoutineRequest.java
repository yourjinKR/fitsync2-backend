package app.fitsync.domain.routine.dto.routine;

import app.fitsync.domain.routine.dto.exercise.RoutineExerciseRequest;
import app.fitsync.domain.routine.entity.Routine;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "루틴 생성 요청")
public record RoutineRequest(

        @NotNull
        @Size(max = Routine.NAME_MAX_LENGTH)
        @Schema(description = "루틴 이름", example = "하체 집중 루틴")
        String name,

        @NotNull
        @Schema(description = "작성자 사용자 ID", example = "1")
        long writerId,

        @NotNull
        @Schema(description = "소유자 사용자 ID", example = "1")
        long ownerId,

        @NotNull
        @Schema(description = "표시 순서", example = "1")
        int displayOrder,

        @NotNull
        @Size(max = Routine.DESCRIPTION_MAX_LENGTH)
        @Schema(description = "루틴 설명", example = "주 2회 하체 강화 프로그램")
        String description,

        @NotNull
        @Valid
        @Schema(description = "루틴 운동 목록")
        List<RoutineExerciseRequest> routineExercises
) {

}
