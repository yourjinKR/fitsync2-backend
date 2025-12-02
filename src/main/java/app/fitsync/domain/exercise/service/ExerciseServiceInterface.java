package app.fitsync.domain.exercise.service;

import app.fitsync.domain.exercise.dto.exercise.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseListResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseRequest;
import app.fitsync.domain.exercise.dto.exercise.ExerciseResponse;
import org.springframework.data.domain.Page;

public interface ExerciseServiceInterface {

    ExerciseResponse createExercise(ExerciseRequest request);

    Page<ExerciseListResponse> getExerciseList(int page, int size);

    ExerciseDetailResponse findExercise(Long id);
}
