package app.fitsync.domain.profile.dto;

import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.profile.entity.WorkoutGoal;
import app.fitsync.domain.user.entity.BirthDate;
import app.fitsync.domain.user.entity.Gender;
import java.util.Set;

public record UserProfileDetailResponse(
        Gender gender,
        BirthDate birth,
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
}
