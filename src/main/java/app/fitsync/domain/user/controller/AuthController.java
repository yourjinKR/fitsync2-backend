package app.fitsync.domain.user.controller;

import app.fitsync.domain.jwt.dto.JWTResponse;
import app.fitsync.domain.user.dto.LoginRequest;
import app.fitsync.domain.user.service.AuthServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthServiceInterface authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<JWTResponse> login(@RequestBody LoginRequest request) {
        JWTResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
