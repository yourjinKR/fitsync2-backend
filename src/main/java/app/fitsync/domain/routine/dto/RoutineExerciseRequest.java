package app.fitsync.domain.routine.dto;

import java.util.List;

public record RoutineExerciseRequest(
        long exerciseId,
        int displayOrder,
        String description,
        List<RoutineSetRequest> routineSets
) {
}
