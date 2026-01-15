package app.fitsync.domain.workout.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutDetailResponse(
        long id,
        long writerId,
        long ownerId,
        String memo,
        LocalDateTime createdAt,

        List<WorkoutExerciseDetailResponse> workoutExercises
) {
}
