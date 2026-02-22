package app.fitsync.domain.user.controller;

import app.fitsync.domain.user.dto.UserDeleteRequest;
import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.dto.UserResponse;
import app.fitsync.domain.user.service.UserServiceInterface;
import app.fitsync.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "사용자", description = "사용자 API")
public class UserController {
    private final UserServiceInterface userService;

    @PostMapping("/api/users")
    @Operation(summary = "회원 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 검증 실패 (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "로그인 ID 중복 (UserException.DUPLICATE_LOGINID)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/users/exists/{loginId}")
    @Operation(summary = "로그인 ID 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<Boolean> existUser(
            @Parameter(description = "로그인 ID", required = true)
            @PathVariable String loginId) {
        Boolean result = userService.existUser(loginId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/api/users/me")
    @Operation(summary = "내 사용자 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않음 (CommonErrorCode.UNAUTHORIZED)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 (CommonErrorCode.ACCESS_DENIED)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자 없음 (UserException.NOT_FOUND_LOGIN_ID)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponse> findMe() {
        UserResponse me = userService.findMe();
        return ResponseEntity.ok(me);
    }

    @DeleteMapping("/api/users/me")
    @Operation(summary = "회원 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 파라미터 (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 (CommonErrorCode.ACCESS_DENIED)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponse> deleteUser(@Valid @RequestBody UserDeleteRequest request) {
        UserResponse response = userService.deleteUser(request);
        return ResponseEntity.ok(response);
    }
}
