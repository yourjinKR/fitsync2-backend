package app.fitsync.domain.workout.controller;

import app.fitsync.domain.workout.dto.WorkoutDetailRequest;
import app.fitsync.domain.workout.dto.WorkoutDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutListRequest;
import app.fitsync.domain.workout.dto.WorkoutListResponse;
import app.fitsync.domain.workout.dto.WorkoutRequest;
import app.fitsync.domain.workout.dto.WorkoutResponse;
import app.fitsync.domain.workout.service.WorkoutServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutServiceInterface workoutService;


    @PostMapping("/api/workout")
    public ResponseEntity<WorkoutResponse> create(@RequestBody WorkoutRequest request) {

        WorkoutResponse workoutResponse = workoutService.create(request);
        return ResponseEntity.ok(workoutResponse);
    }


    @GetMapping("/api/workouts")
    public ResponseEntity<Page<WorkoutListResponse>> viewList(
            @RequestParam(required = false) Long writerId,
            @RequestParam(required = false) Long ownerId,
            @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {

        WorkoutListRequest request = new WorkoutListRequest(ownerId);
        Page<WorkoutListResponse> workoutListResponses = workoutService.viewList(request, pageable);

        return ResponseEntity.ok(workoutListResponses);
    }

    @GetMapping("/api/workout")
    public ResponseEntity<WorkoutDetailResponse> viewDetail(@RequestParam long id) {

        WorkoutDetailRequest request = new WorkoutDetailRequest(id);
        WorkoutDetailResponse response = workoutService.viewDetail(request);

        return ResponseEntity.ok(response);
    }
}
