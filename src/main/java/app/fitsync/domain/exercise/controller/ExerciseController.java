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
@Tag(name = "Exercise", description = "Exercise master data API")
public class ExerciseController {
    private final ExerciseServiceInterface exerciseService;

    @PostMapping("/api/exercise")
    @Operation(summary = "Create exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create success"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody ExerciseRequest request) {
        ExerciseResponse response = exerciseService.createExercise(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/exercises")
    @Operation(summary = "Get exercise list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success")
    })
    public ResponseEntity<Page<ExerciseListResponse>> getExerciseList(
            @RequestParam(required = false) ExerciseCategory category,
            @RequestParam(required = false, defaultValue = "false") boolean hidden,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        Page<ExerciseListResponse> responsePage = exerciseService.getExerciseList(pageable, category, hidden);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/api/exercise/{exerciseId}")
    @Operation(summary = "Get exercise detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exercise not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ExerciseDetailResponse> getExerciseDetail(
            @Parameter(description = "Exercise ID", required = true)
            @PathVariable Long exerciseId) {
        ExerciseDetailResponse response = exerciseService.findExercise(exerciseId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/exercise/{exerciseId}")
    @Operation(summary = "Update exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exercise not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ExerciseResponse> updateExercise(
            @Parameter(description = "Exercise ID", required = true)
            @PathVariable Long exerciseId,
            @Valid @RequestBody ExerciseUpdateRequest request) {
        ExerciseResponse response = exerciseService.updateExercise(exerciseId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/exercise/{exerciseId}")
    @Operation(summary = "Delete exercise", description = "Delete by SOFT or HARD delete type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exercise not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ExerciseResponse> deleteExercise(
            @Parameter(description = "Exercise ID", required = true)
            @PathVariable Long exerciseId,
            @Parameter(description = "Delete type")
            @RequestParam(defaultValue = "SOFT") DeleteType deleteType) {
        ExerciseResponse response = exerciseService.deleteExercise(exerciseId, deleteType);
        return ResponseEntity.ok(response);
    }
}
