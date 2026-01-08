package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import java.util.List;

public interface AIServiceInterface {
    List<AIRoutineResponse> generateRoutine(AIRoutineRequest request);
}
