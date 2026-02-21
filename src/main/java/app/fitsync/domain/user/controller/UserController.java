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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserServiceInterface userService;

    @PostMapping("/api/user")
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create success"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Duplicate loginId",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/user/exist/{loginId}")
    @Operation(summary = "Check loginId duplication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success")
    })
    public ResponseEntity<Boolean> existUser(
            @Parameter(description = "Login ID", required = true)
            @PathVariable String loginId) {
        Boolean result = userService.existUser(loginId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/api/user/me")
    @Operation(summary = "Get my user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthenticated",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponse> findMe() {
        UserResponse me = userService.findMe();
        return ResponseEntity.ok(me);
    }

    @DeleteMapping("/api/user")
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete success"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponse> deleteUser(@RequestBody UserDeleteRequest request) {
        UserResponse response = userService.deleteUser(request);
        return ResponseEntity.ok(response);
    }
}
