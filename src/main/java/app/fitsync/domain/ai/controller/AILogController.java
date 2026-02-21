package app.fitsync.domain.ai.controller;

import app.fitsync.domain.ai.dto.AILogResponse;
import app.fitsync.domain.ai.service.AILogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Tag(name = "AI", description = "AI 로그 API")
public class AILogController {

    private final AILogService aiLogService;

    @GetMapping("/api/ai/log/routine/{id}")
    @Operation(summary = "AI 루틴 로그 상세 조회")
    public ResponseEntity<AILogResponse> generateRoutine(
            @Parameter(description = "AI 로그 ID", required = true)
            @PathVariable long id) {
        AILogResponse response = aiLogService.viewDetail(id);
        return ResponseEntity.ok(response);
    }
}
