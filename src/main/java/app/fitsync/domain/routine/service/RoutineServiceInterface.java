package app.fitsync.domain.routine.service;

import app.fitsync.domain.routine.dto.routine.RoutineRequest;
import app.fitsync.domain.routine.dto.routine.RoutineResponse;

public interface RoutineServiceInterface {
    public RoutineResponse create(RoutineRequest request);
}
