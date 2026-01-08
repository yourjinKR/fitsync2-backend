package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface AIServiceInterface {
    AIRoutineResponse generateRoutine(AIRoutineRequest request) throws JsonProcessingException;
}
