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
@Tag(name = "루틴", description = "루틴 관리 API")
public class RoutineController {

    private final RoutineServiceInterface routineService;

    @PostMapping("/api/routines")
    @Operation(summary = "루틴 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 검증 실패 (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RoutineResponse> createRoutine(@Valid @RequestBody RoutineRequest request) {
        RoutineResponse response = routineService.createRoutine(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/routines")
    @Operation(summary = "루틴 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<Page<RoutineListResponse>> getRoutineList(
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long writerId,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        Page<RoutineListResponse> responsePage = routineService.getRoutineList(pageable, ownerId, writerId);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/api/routines/{routineId}")
    @Operation(summary = "루틴 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "루틴 없음 (RoutineErrorCode.NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RoutineDetailResponse> getRoutineDetail(
            @Parameter(description = "루틴 ID", required = true)
            @PathVariable long routineId) {
        RoutineDetailResponse response = routineService.findRoutine(routineId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/routines/{routineId}")
    @Operation(summary = "루틴 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 검증 실패 (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "루틴/운동/세트 없음 (RoutineErrorCode.NOT_FOUND, RoutineErrorCode.EXERCISE_NOT_FOUND, RoutineErrorCode.SET_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RoutineResponse> updateRoutine(
            @Parameter(description = "루틴 ID", required = true)
            @PathVariable long routineId,
            @Valid @RequestBody RoutineUpdateRequest request) {
        RoutineResponse response = routineService.updateRoutine(routineId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/routines/{routineId}")
    @Operation(summary = "루틴 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "루틴 없음 (RoutineErrorCode.NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RoutineResponse> deleteRoutine(
            @Parameter(description = "루틴 ID", required = true)
            @PathVariable long routineId) {
        RoutineResponse response = routineService.deleteRoutine(routineId);
        return ResponseEntity.ok(response);
    }
}
