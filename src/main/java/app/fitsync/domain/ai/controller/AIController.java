package app.fitsync.domain.ai.controller;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import app.fitsync.domain.ai.service.AIServiceInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AIController {

    private final AIServiceInterface aiService;

    @PostMapping("/api/ai/routine")
    public ResponseEntity<AIRoutineResponse> generateRoutine(@RequestBody AIRoutineRequest request)
            throws JsonProcessingException {
        AIRoutineResponse response = aiService.generateRoutine(request);
        return ResponseEntity.ok(response);
    }
}
