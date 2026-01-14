package app.fitsync.domain.workout.dto;

import java.util.List;

public record WorkoutRequest(
        long writerId,
        long ownerId,
        String memo,
        List<WorkoutExerciseRequest> workoutExercises
) {
}
