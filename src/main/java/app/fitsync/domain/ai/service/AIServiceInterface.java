package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;

public interface AIServiceInterface {
    String generateTest(String text);
    String generateRoutine(AIRoutineRequest request);
}
