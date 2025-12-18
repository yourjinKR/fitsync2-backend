package app.fitsync.domain.routine.dto.exercise;

import app.fitsync.domain.routine.dto.set.RoutineSetDetailResponse;
import java.util.List;

public record RoutineExerciseDetailResponse(
        long id,
        long exerciseId,
        String exerciseName,
        int displayOrder,
        String description,
        List<RoutineSetDetailResponse> routineSets
) {
}
