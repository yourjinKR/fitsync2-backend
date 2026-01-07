package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;

public interface AIServiceInterface {
    String generateTest(String text);
    AIRoutineResponse generateRoutine(AIRoutineRequest request);
}
