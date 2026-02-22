package app.fitsync.domain.workout.controller;

import app.fitsync.domain.workout.dto.WorkoutDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutListRequest;
import app.fitsync.domain.workout.dto.WorkoutListResponse;
import app.fitsync.domain.workout.dto.WorkoutRequest;
import app.fitsync.domain.workout.dto.WorkoutResponse;
import app.fitsync.domain.workout.service.WorkoutServiceInterface;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "운동기록", description = "실제 운동 기록 API")
public class WorkoutController {

    private final WorkoutServiceInterface workoutService;

    @PostMapping("/api/workouts")
    @Operation(summary = "운동 기록 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 검증 실패 (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "참조 사용자/운동 없음 (UserException.NOT_FOUND, ExerciseErrorCode.NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<WorkoutResponse> createWorkout(@Valid @RequestBody WorkoutRequest request) {
        WorkoutResponse workoutResponse = workoutService.create(request);
        return ResponseEntity.ok(workoutResponse);
    }

    @GetMapping("/api/workouts")
    @Operation(summary = "운동 기록 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<Page<WorkoutListResponse>> getWorkoutList(
            @RequestParam(required = false) Long ownerId,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        WorkoutListRequest request = new WorkoutListRequest(ownerId);
        Page<WorkoutListResponse> workoutListResponses = workoutService.viewList(request, pageable);
        return ResponseEntity.ok(workoutListResponses);
    }

    @GetMapping("/api/workouts/{id}")
    @Operation(summary = "운동 기록 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 ID (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<WorkoutDetailResponse> getWorkoutDetail(
            @Parameter(description = "운동 기록 ID", required = true)
            @PathVariable long id) {
        WorkoutDetailResponse response = workoutService.viewDetail(id);
        return ResponseEntity.ok(response);
    }
}
