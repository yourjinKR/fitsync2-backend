package app.fitsync.domain.workout.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Workout detail response")
public record WorkoutDetailResponse(
        long id,
        long writerId,
        long ownerId,
        String memo,
        LocalDateTime createdAt,

        List<WorkoutExerciseDetailResponse> workoutExercises
) {
}
