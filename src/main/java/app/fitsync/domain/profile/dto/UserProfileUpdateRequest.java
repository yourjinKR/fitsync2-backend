package app.fitsync.domain.profile.dto;

import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.profile.entity.WorkoutGoal;
import app.fitsync.domain.user.entity.Gender;
import java.time.LocalDate;
import java.util.Set;

public record UserProfileUpdateRequest(
        long id,
        Gender gender,
        LocalDate localDateBirth,
        Set<WorkoutGoal> workoutGoals,
        Set<ExerciseCategory> exerciseCategories,
        String disease,
        Double height
) {
}
