package app.fitsync.domain.exercise.service;

import app.fitsync.domain.exercise.dto.exercise.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseListResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseRequest;
import app.fitsync.domain.exercise.dto.exercise.ExerciseResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseUpdateRequest;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.global.DeleteType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExerciseServiceInterface {

    ExerciseResponse createExercise(ExerciseRequest request);

    Page<ExerciseListResponse> getExerciseList(Pageable pageable, ExerciseCategory category, boolean hidden);

    ExerciseDetailResponse findExercise(Long id);

    ExerciseResponse updateExercise(Long id, ExerciseUpdateRequest request);

    ExerciseResponse deleteExercise(Long id, DeleteType deleteType);
}
