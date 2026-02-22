package app.fitsync.domain.exercise.controller;

import app.fitsync.domain.exercise.dto.exercise.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseListResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseRequest;
import app.fitsync.domain.exercise.dto.exercise.ExerciseResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseUpdateRequest;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.service.ExerciseServiceInterface;
import app.fitsync.global.DeleteType;
import app.fitsync.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@NullMarked
@RestController
@RequiredArgsConstructor
@Tag(name = "운동", description = "운동 마스터 데이터 API")
public class ExerciseController {
    private final ExerciseServiceInterface exerciseService;

    @PostMapping("/api/exercises")
    @Operation(summary = "운동 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 검증 실패 (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody ExerciseRequest request) {
        ExerciseResponse response = exerciseService.createExercise(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/api/exercises")
    @Operation(summary = "운동 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<Page<ExerciseListResponse>> getExerciseList(
            @RequestParam(required = false) ExerciseCategory category,
            @RequestParam(required = false, defaultValue = "false") boolean hidden,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        Page<ExerciseListResponse> responsePage = exerciseService.getExerciseList(pageable, category, hidden);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/api/exercises/{exerciseId}")
    @Operation(summary = "운동 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "운동 없음 (ExerciseErrorCode.NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ExerciseDetailResponse> getExerciseDetail(
            @Parameter(description = "운동 ID", required = true)
            @PathVariable Long exerciseId) {
        ExerciseDetailResponse response = exerciseService.findExercise(exerciseId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/exercises/{exerciseId}")
    @Operation(summary = "운동 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 검증 실패 (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "운동/타겟 없음 (ExerciseErrorCode.NOT_FOUND, ExerciseErrorCode.TARGET_NOT_FOUNT)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ExerciseResponse> updateExercise(
            @Parameter(description = "운동 ID", required = true)
            @PathVariable Long exerciseId,
            @Valid @RequestBody ExerciseUpdateRequest request) {
        ExerciseResponse response = exerciseService.updateExercise(exerciseId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/exercises/{exerciseId}")
    @Operation(summary = "운동 삭제", description = "SOFT/HARD 삭제 유형으로 운동을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "운동 없음 (ExerciseErrorCode.NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ExerciseResponse> deleteExercise(
            @Parameter(description = "운동 ID", required = true)
            @PathVariable Long exerciseId,
            @Parameter(description = "삭제 유형")
            @RequestParam(defaultValue = "SOFT") DeleteType deleteType) {
        ExerciseResponse response = exerciseService.deleteExercise(exerciseId, deleteType);
        return ResponseEntity.ok(response);
    }
}
