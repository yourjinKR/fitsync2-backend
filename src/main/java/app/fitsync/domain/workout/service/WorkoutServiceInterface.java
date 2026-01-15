package app.fitsync.domain.workout.service;

import app.fitsync.domain.workout.dto.WorkoutDetailRequest;
import app.fitsync.domain.workout.dto.WorkoutDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutListRequest;
import app.fitsync.domain.workout.dto.WorkoutListResponse;
import app.fitsync.domain.workout.dto.WorkoutRequest;
import app.fitsync.domain.workout.dto.WorkoutResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkoutServiceInterface {

    WorkoutResponse create(WorkoutRequest request);
    Page<WorkoutListResponse> viewList(WorkoutListRequest request, Pageable pageable);
    WorkoutDetailResponse viewDetail(WorkoutDetailRequest request);
}
