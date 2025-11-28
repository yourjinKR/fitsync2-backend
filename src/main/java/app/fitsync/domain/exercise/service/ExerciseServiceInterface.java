package app.fitsync.domain.exercise.service;

import app.fitsync.domain.exercise.dto.ExerciseRequest;
import app.fitsync.domain.exercise.dto.ExerciseResponse;

public interface ExerciseServiceInterface {
    ExerciseResponse createExercise(ExerciseRequest request);
}
