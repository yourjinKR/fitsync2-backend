package app.fitsync.domain.routine.service;

import app.fitsync.domain.routine.dto.RoutineRequest;
import app.fitsync.domain.routine.dto.RoutineResponse;

public interface RoutineServiceInterface {
    public RoutineResponse create(RoutineRequest request);
}
