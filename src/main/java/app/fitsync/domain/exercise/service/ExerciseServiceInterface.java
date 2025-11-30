package app.fitsync.domain.exercise.service;

import app.fitsync.domain.exercise.dto.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.ExerciseListResponse;
import app.fitsync.domain.exercise.dto.ExerciseRequest;
import app.fitsync.domain.exercise.dto.ExerciseResponse;
import org.springframework.data.domain.Page;

public interface ExerciseServiceInterface {

    ExerciseResponse createExercise(ExerciseRequest request);

    Page<ExerciseListResponse> getExerciseList(int page, int size);

    ExerciseDetailResponse findExercise(Long id);
}
