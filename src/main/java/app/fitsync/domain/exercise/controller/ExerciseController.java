package app.fitsync.domain.exercise.controller;

import app.fitsync.domain.exercise.dto.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.ExerciseRequest;
import app.fitsync.domain.exercise.dto.ExerciseResponse;
import app.fitsync.domain.exercise.service.ExerciseServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseServiceInterface exerciseService;

    @PostMapping("/api/exercise")
    public ResponseEntity<ExerciseResponse> createExercise(@RequestBody ExerciseRequest request) {
        ExerciseResponse response = exerciseService.createExercise(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/exercise/{exerciseId}")
    public ResponseEntity<ExerciseDetailResponse> findExercise(@PathVariable Long exerciseId) {
        ExerciseDetailResponse response = exerciseService.findExercise(exerciseId);
        return ResponseEntity.ok(response);
    }
}
