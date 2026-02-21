package app.fitsync.domain.jwt.controller;

import app.fitsync.domain.jwt.dto.JWTResponse;
import app.fitsync.domain.jwt.dto.RefreshRequest;
import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "토큰", description = "JWT 토큰 보조 API")
public class JwtController {

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/jwt/exchange")
    @Operation(summary = "쿠키 refresh 토큰 교환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교환 성공"),
            @ApiResponse(
                    responseCode = "500",
                    description = "유효하지 않은 쿠키 또는 refresh 토큰",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public JWTResponse jwtExchangeApi(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return jwtService.cookie2Header(request, response);
    }

    @PostMapping(value = "/jwt/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "refresh 토큰 재발급(rotate)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(
                    responseCode = "500",
                    description = "유효하지 않은 refresh 토큰",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public JWTResponse jwtRefreshApi(
            @Validated @RequestBody RefreshRequest dto
    ) {
        return jwtService.refreshRotate(dto);
    }
}
