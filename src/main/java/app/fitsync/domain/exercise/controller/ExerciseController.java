package app.fitsync.domain.exercise.controller;

import app.fitsync.domain.exercise.dto.exercise.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseListResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseRequest;
import app.fitsync.domain.exercise.dto.exercise.ExerciseResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseUpdateRequest;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.service.ExerciseServiceInterface;
import app.fitsync.global.DeleteType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@NullMarked
@RestController
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseServiceInterface exerciseService;

    @PostMapping("/api/exercise")
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody ExerciseRequest request) {

        ExerciseResponse response = exerciseService.createExercise(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/exercises")
    public ResponseEntity<Page<ExerciseListResponse>> getExerciseList(
            @RequestParam(required = false) ExerciseCategory category,
            @RequestParam(required = false, defaultValue = "false") boolean hidden,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {

        Page<ExerciseListResponse> responsePage = exerciseService.getExerciseList(pageable, category, hidden);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/api/exercise/{exerciseId}")
    public ResponseEntity<ExerciseDetailResponse> getExerciseDetail(@PathVariable Long exerciseId) {

        ExerciseDetailResponse response = exerciseService.findExercise(exerciseId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/exercise/{exerciseId}")
    public ResponseEntity<ExerciseResponse> updateExercise(
            @PathVariable Long exerciseId,
            @Valid @RequestBody ExerciseUpdateRequest request) {

        ExerciseResponse response = exerciseService.updateExercise(exerciseId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/exercise/{exerciseId}")
    public ResponseEntity<ExerciseResponse> deleteExercise(
            @PathVariable Long exerciseId,
            @RequestParam(defaultValue = "SOFT") DeleteType deleteType) {

        ExerciseResponse response = exerciseService.deleteExercise(exerciseId, deleteType);
        return ResponseEntity.ok(response);
    }
}
