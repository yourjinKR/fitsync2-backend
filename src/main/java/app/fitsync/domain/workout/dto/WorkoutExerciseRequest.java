package app.fitsync.domain.workout.dto;

import java.util.List;

public record WorkoutExerciseRequest(
        long exerciseId,
        String memo,
        List<WorkoutSetRequest> sets

) {
}
