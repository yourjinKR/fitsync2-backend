package app.fitsync.domain.user.controller;

import app.fitsync.domain.jwt.dto.JWTResponse;
import app.fitsync.domain.user.dto.LoginRequest;
import app.fitsync.domain.user.service.AuthServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Auth", description = "인증 API")
public class AuthController {
    private final AuthServiceInterface authService;

    @PostMapping("/api/auth/login")
    @Operation(summary = "로그인")
    public ResponseEntity<JWTResponse> login(@RequestBody LoginRequest request) {
        JWTResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그아웃 엔드포인트
     * 현재 인증된 사용자의 Refresh 토큰을 삭제하여 로그아웃 처리합니다.
     * 클라이언트에서는 localStorage의 토큰들도 제거해야 합니다.
     *
     * @return 로그아웃 성공 응답
     */
    @PostMapping("/api/auth/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }
}
