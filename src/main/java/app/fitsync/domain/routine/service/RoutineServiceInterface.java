package app.fitsync.domain.routine.service;

import app.fitsync.domain.routine.dto.routine.RoutineDetailResponse;
import app.fitsync.domain.routine.dto.routine.RoutineListResponse;
import app.fitsync.domain.routine.dto.routine.RoutineRequest;
import app.fitsync.domain.routine.dto.routine.RoutineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoutineServiceInterface {
    RoutineResponse createRoutine(RoutineRequest request);
    Page<RoutineListResponse> getRoutineList(Pageable pageable, Long ownerId, Long writerId);
    RoutineDetailResponse findRoutine(long routineId);
}
