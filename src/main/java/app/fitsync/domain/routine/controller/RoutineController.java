package app.fitsync.domain.routine.controller;

import app.fitsync.domain.routine.dto.routine.RoutineDetailResponse;
import app.fitsync.domain.routine.dto.routine.RoutineListResponse;
import app.fitsync.domain.routine.dto.routine.RoutineRequest;
import app.fitsync.domain.routine.dto.routine.RoutineResponse;
import app.fitsync.domain.routine.dto.routine.RoutineUpdateRequest;
import app.fitsync.domain.routine.service.RoutineServiceInterface;
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

@RestController
@RequiredArgsConstructor
@NullMarked
@Tag(name = "Routine", description = "Routine API")
public class RoutineController {

    private final RoutineServiceInterface routineService;

    @PostMapping("/api/routine")
    @Operation(summary = "Create routine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create success"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RoutineResponse> createRoutine(@Valid @RequestBody RoutineRequest request) {
        RoutineResponse response = routineService.createRoutine(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/routines")
    @Operation(summary = "Get routine list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success")
    })
    public ResponseEntity<Page<RoutineListResponse>> getRoutineList(
            @RequestParam(required = false) Long writerId,
            @RequestParam(required = false) Long ownerId,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        Page<RoutineListResponse> responsePage = routineService.getRoutineList(pageable, writerId, ownerId);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/api/routine/{routineId}")
    @Operation(summary = "Get routine detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Routine not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RoutineDetailResponse> getRoutineDetail(
            @Parameter(description = "Routine ID", required = true)
            @PathVariable long routineId) {
        RoutineDetailResponse response = routineService.findRoutine(routineId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/routine/{routineId}")
    @Operation(summary = "Update routine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Routine not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RoutineResponse> updateRoutine(
            @Parameter(description = "Routine ID", required = true)
            @PathVariable long routineId,
            @RequestBody RoutineUpdateRequest request) {
        RoutineResponse response = routineService.updateRoutine(routineId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/routine/{routineId}")
    @Operation(summary = "Delete routine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Routine not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RoutineResponse> deleteRoutine(
            @Parameter(description = "Routine ID", required = true)
            @PathVariable long routineId) {
        RoutineResponse response = routineService.deleteRoutine(routineId);
        return ResponseEntity.ok(response);
    }
}
