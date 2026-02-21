package app.fitsync.domain.routine.dto.routine;

import app.fitsync.domain.routine.dto.exercise.RoutineExerciseDetailResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "루틴 상세 응답")
public record RoutineDetailResponse(
        long id,
        String name,
        int displayOrder,
        String description,
        List<RoutineExerciseDetailResponse> routineExercises
) {
}
