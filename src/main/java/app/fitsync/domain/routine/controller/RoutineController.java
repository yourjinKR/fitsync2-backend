package app.fitsync.domain.routine.controller;

import app.fitsync.domain.routine.dto.routine.RoutineDetailResponse;
import app.fitsync.domain.routine.dto.routine.RoutineListResponse;
import app.fitsync.domain.routine.dto.routine.RoutineRequest;
import app.fitsync.domain.routine.dto.routine.RoutineResponse;
import app.fitsync.domain.routine.dto.routine.RoutineUpdateRequest;
import app.fitsync.domain.routine.service.RoutineServiceInterface;
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
public class RoutineController {

    private final RoutineServiceInterface routineService;

    @PostMapping("/api/routine")
    public ResponseEntity<RoutineResponse> createRoutine(@Valid @RequestBody RoutineRequest request) {

        RoutineResponse response = routineService.createRoutine(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/routines")
    public ResponseEntity<Page<RoutineListResponse>> getRoutineList(
            @RequestParam(required = false) Long writerId,
            @RequestParam(required = false) Long ownerId,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {

        Page<RoutineListResponse> responsePage = routineService.getRoutineList(pageable, writerId, ownerId);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/api/routine/{routineId}")
    public ResponseEntity<RoutineDetailResponse> getRoutineDetail(@PathVariable long routineId) {

        RoutineDetailResponse response = routineService.findRoutine(routineId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/routine/{routineId}")
    public ResponseEntity<RoutineResponse> updateRoutine(
            @PathVariable long routineId,
            @RequestBody RoutineUpdateRequest request) {

        RoutineResponse response = routineService.updateRoutine(routineId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/routine/{routineId}")
    public ResponseEntity<RoutineResponse> deleteRoutine(@PathVariable long routineId) {
        RoutineResponse response = routineService.deleteRoutine(routineId);
        return ResponseEntity.ok(response);
    }
}
