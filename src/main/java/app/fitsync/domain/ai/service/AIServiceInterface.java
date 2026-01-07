package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import java.util.List;

public interface AIServiceInterface {
    String generateTest(String text);
    List<AIRoutineResponse> generateRoutine(AIRoutineRequest request);
}
