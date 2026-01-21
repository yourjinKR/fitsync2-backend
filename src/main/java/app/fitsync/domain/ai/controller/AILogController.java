package app.fitsync.domain.ai.controller;

import app.fitsync.domain.ai.dto.AILogResponse;
import app.fitsync.domain.ai.service.AILogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AILogController {

    private final AILogService aiLogService;

    @GetMapping("/api/ai/log/routine/{id}")
    public ResponseEntity<AILogResponse> generateRoutine(@PathVariable long id) {
        AILogResponse response = aiLogService.viewDetail(id);
        return ResponseEntity.ok(response);
    }
}
