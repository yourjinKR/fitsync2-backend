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
@Tag(name = "Workout", description = "Workout record API")
public class WorkoutController {

    private final WorkoutServiceInterface workoutService;

    @PostMapping("/api/workout")
    @Operation(summary = "Create workout record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Referenced user or exercise not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<WorkoutResponse> createWorkout(@RequestBody WorkoutRequest request) {
        WorkoutResponse workoutResponse = workoutService.create(request);
        return ResponseEntity.ok(workoutResponse);
    }

    @GetMapping("/api/workouts")
    @Operation(summary = "Get workout records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success")
    })
    public ResponseEntity<Page<WorkoutListResponse>> getWorkoutList(
            @RequestParam(required = false) Long writerId,
            @RequestParam(required = false) Long ownerId,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        WorkoutListRequest request = new WorkoutListRequest(ownerId);
        Page<WorkoutListResponse> workoutListResponses = workoutService.viewList(request, pageable);
        return ResponseEntity.ok(workoutListResponses);
    }

    @GetMapping("/api/workout/{id}")
    @Operation(summary = "Get workout detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid id",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<WorkoutDetailResponse> getWorkoutDetail(
            @Parameter(description = "Workout ID", required = true)
            @PathVariable long id) {
        WorkoutDetailResponse response = workoutService.viewDetail(id);
        return ResponseEntity.ok(response);
    }
}
