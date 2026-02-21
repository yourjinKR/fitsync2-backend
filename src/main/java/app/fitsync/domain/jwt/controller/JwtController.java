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
@Tag(name = "JWT", description = "JWT support API")
public class JwtController {

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/jwt/exchange")
    @Operation(summary = "Exchange cookie refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchange success"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Invalid cookie or refresh token",
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
    @Operation(summary = "Rotate refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rotate success"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Invalid refresh token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public JWTResponse jwtRefreshApi(
            @Validated @RequestBody RefreshRequest dto
    ) {
        return jwtService.refreshRotate(dto);
    }
}
