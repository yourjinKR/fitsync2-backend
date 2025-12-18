package app.fitsync.domain.routine.dto.routine;

import app.fitsync.domain.routine.dto.exercise.RoutineExerciseDetailResponse;
import java.util.List;

public record RoutineDetailResponse(
        long id,
        String name,
        int displayOrder,
        String description,
        List<RoutineExerciseDetailResponse> routineExercises
) {
}
