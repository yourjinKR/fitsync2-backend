package app.fitsync.domain.workout.dto;

import app.fitsync.domain.exercise.dto.exercise.ExerciseSummaryResponse;
import java.util.List;

public record WorkoutExerciseDetailResponse(
        long id,
        ExerciseSummaryResponse exercise,
        String memo,

        List<WorkoutSetDetailResponse> workoutSets
) {

}
