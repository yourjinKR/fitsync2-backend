package app.fitsync.domain.ai.controller;

import app.fitsync.domain.ai.service.AIServiceInterface;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AIController {

    private final AIServiceInterface aiService;

    @PostMapping("/api/ai/chat/test")
    public ResponseEntity<String> chat(@RequestBody Map<String, String> body) {
        String result = aiService.generate(body.get("text"));
        return ResponseEntity.ok(result);
    }
}
