package app.fitsync.domain.profile.dto;

import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.profile.entity.WorkoutGoal;
import app.fitsync.domain.user.entity.Gender;
import java.time.LocalDateTime;
import java.util.Set;


public record UserProfileRequest(

        long userId,

        Gender gender,

        LocalDateTime birth,

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
