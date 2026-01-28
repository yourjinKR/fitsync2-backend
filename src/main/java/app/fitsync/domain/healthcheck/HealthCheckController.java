package app.fitsync.domain.healthcheck;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/api/check")
    public ResponseEntity<Map<String, Object>> check() {
        return ResponseEntity.ok(Map.of("connected", "true"));
    }
}
