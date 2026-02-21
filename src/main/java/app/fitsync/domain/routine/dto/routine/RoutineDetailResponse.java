package app.fitsync.domain.routine.dto.routine;

import app.fitsync.domain.routine.dto.exercise.RoutineExerciseDetailResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Routine detail response")
public record RoutineDetailResponse(
        long id,
        String name,
        int displayOrder,
        String description,
        List<RoutineExerciseDetailResponse> routineExercises
) {
}
