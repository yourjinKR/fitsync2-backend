package app.fitsync.domain.workout.service;

import app.fitsync.domain.workout.dto.WorkoutRequest;
import app.fitsync.domain.workout.dto.WorkoutResponse;

public interface WorkoutServiceInterface {

    WorkoutResponse create(WorkoutRequest request);
}
