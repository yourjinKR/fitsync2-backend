package app.fitsync.domain.workout.controller;

import app.fitsync.domain.workout.dto.WorkoutRequest;
import app.fitsync.domain.workout.dto.WorkoutResponse;
import app.fitsync.domain.workout.service.WorkoutServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
}
