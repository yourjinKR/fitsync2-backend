package app.fitsync.domain.healthcheck;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "헬스체크", description = "헬스체크 API")
public class HealthCheckController {

    @GetMapping("/api/check")
    @Operation(summary = "서버 상태 확인")
    public ResponseEntity<Map<String, Object>> check() {
        return ResponseEntity.ok(Map.of("connected", "true"));
    }
}
