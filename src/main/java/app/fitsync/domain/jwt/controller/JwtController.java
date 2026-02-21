package app.fitsync.domain.jwt.controller;

import app.fitsync.domain.jwt.dto.JWTResponse;
import app.fitsync.domain.jwt.dto.RefreshRequest;
import app.fitsync.domain.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "JWT", description = "JWT 토큰 보조 API")
public class JwtController {

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // 소셜 로그인 쿠키 방식의 Refresh 토큰 헤더 방식으로 교환
    @PostMapping(value = "/jwt/exchange")
    @Operation(summary = "쿠키 refresh 토큰 교환")
    public JWTResponse jwtExchangeApi(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return jwtService.cookie2Header(request, response);
    }

    // Refresh 토큰으로 Access 토큰 재발급 (Rotate 포함)
    @PostMapping(value = "/jwt/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "refresh 토큰 재발급(rotate)")
    public JWTResponse jwtRefreshApi(
            @Validated @RequestBody RefreshRequest dto
    ) {
        return jwtService.refreshRotate(dto);
    }

}
