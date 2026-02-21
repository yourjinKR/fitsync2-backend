package app.fitsync.domain.exercise.controller;

import app.fitsync.domain.exercise.dto.exercise.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseListResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseRequest;
import app.fitsync.domain.exercise.dto.exercise.ExerciseResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseUpdateRequest;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.service.ExerciseServiceInterface;
import app.fitsync.global.DeleteType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Exercise", description = "운동 마스터 데이터 API")
public class ExerciseController {
    private final ExerciseServiceInterface exerciseService;

    @PostMapping("/api/exercise")
    @Operation(summary = "운동 생성", description = "운동 마스터 데이터를 생성합니다.")
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody ExerciseRequest request) {

        ExerciseResponse response = exerciseService.createExercise(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/exercises")
    @Operation(summary = "운동 목록 조회", description = "카테고리/hidden 조건으로 운동 목록을 페이지 조회합니다.")
    public ResponseEntity<Page<ExerciseListResponse>> getExerciseList(
            @RequestParam(required = false) ExerciseCategory category,
            @RequestParam(required = false, defaultValue = "false") boolean hidden,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {

        Page<ExerciseListResponse> responsePage = exerciseService.getExerciseList(pageable, category, hidden);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/api/exercise/{exerciseId}")
    @Operation(summary = "운동 상세 조회")
    public ResponseEntity<ExerciseDetailResponse> getExerciseDetail(
            @Parameter(description = "운동 ID", required = true)
            @PathVariable Long exerciseId) {

        ExerciseDetailResponse response = exerciseService.findExercise(exerciseId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/exercise/{exerciseId}")
    @Operation(summary = "운동 수정")
    public ResponseEntity<ExerciseResponse> updateExercise(
            @Parameter(description = "운동 ID", required = true)
            @PathVariable Long exerciseId,
            @Valid @RequestBody ExerciseUpdateRequest request) {

        ExerciseResponse response = exerciseService.updateExercise(exerciseId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/exercise/{exerciseId}")
    @Operation(summary = "운동 삭제", description = "SOFT/HARD 삭제 타입으로 운동을 삭제합니다.")
    public ResponseEntity<ExerciseResponse> deleteExercise(
            @Parameter(description = "운동 ID", required = true)
            @PathVariable Long exerciseId,
            @Parameter(description = "삭제 타입", required = false)
            @RequestParam(defaultValue = "SOFT") DeleteType deleteType) {

        ExerciseResponse response = exerciseService.deleteExercise(exerciseId, deleteType);
        return ResponseEntity.ok(response);
    }
}
