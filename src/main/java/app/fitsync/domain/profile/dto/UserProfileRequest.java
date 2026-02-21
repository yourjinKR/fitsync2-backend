package app.fitsync.domain.profile.dto;

import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.profile.entity.WorkoutGoal;
import app.fitsync.domain.user.entity.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;

public record UserProfileRequest(
        @NotNull
        Gender gender,

        @NotNull
        LocalDate birth,

        @NotNull
        Set<WorkoutGoal> workoutGoals,

        @NotNull
        Set<ExerciseCategory> exerciseCategories,

        String disease,

        @NotNull
        @Positive
        Double height,

        @NotNull
        @Positive
        Double weight,

        @NotNull
        @Positive
        Double skeletalMuscleMass,

        @NotNull
        @Positive
        Double bodyFatMass,

        @NotNull
        @Positive
        Double bodyFatPercentage,

        @NotNull
        @Positive
        Double bmi
) {
}
