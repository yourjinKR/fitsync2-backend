package app.fitsync.domain.profile.dto;

import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.profile.entity.WorkoutGoal;
import java.util.Set;

public record UserProfileDetailResponse(
        long id,
        User user,
        Set<WorkoutGoal> workoutGoals,
        Set<ExerciseCategory> exerciseCategories,
        String disease,
        Double height,
        Double weight,
        Double skeletalMuscleMass,
        Double bodyFatMass,
        Double bodyFatPercentage,
        Double bmi
) {

    public record User(
            String name,
            Long age,
            boolean hidden
    ) {

    }
}
