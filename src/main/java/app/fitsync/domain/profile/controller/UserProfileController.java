package app.fitsync.domain.profile.controller;

import app.fitsync.domain.profile.dto.InBodyRecordMeRequest;
import app.fitsync.domain.profile.dto.InBodyRecordResponse;
import app.fitsync.domain.profile.dto.InBodyStatisticsResponse;
import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserProfileResponse;
import app.fitsync.domain.profile.dto.UserWithProfileResponse;
import app.fitsync.domain.profile.service.UserProfileServiceInterface;
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
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@NullMarked
@RestController
@RequiredArgsConstructor
@Tag(name = "프로필", description = "사용자 프로필/인바디 API")
public class UserProfileController {

    private final UserProfileServiceInterface userProfileService;

    @PostMapping("/api/users/me/profile")
    @Operation(summary = "프로필 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 검증 실패 (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "프로필 중복 (UserProfileException.DUPLICATE)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserProfileResponse> createProfile(@Valid @RequestBody UserProfileRequest request) {
        UserProfileResponse response = userProfileService.create(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/users/me/inbody-records")
    @Operation(summary = "내 인바디 기록 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 검증 실패 (CommonErrorCode.INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "프로필 없음 (UserProfileException.NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<InBodyRecordResponse> createMyInBody(
            @Valid @RequestBody InBodyRecordMeRequest request
    ) {
        InBodyRecordResponse response = userProfileService.createMyInBody(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/users/{userId}/profile")
    @Operation(summary = "사용자 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "프로필/인바디 없음 (UserProfileException.NOT_FOUND, InBodyException.NOT_FOUND_PROFILE_ID)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserWithProfileResponse> getProfile(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable long userId) {
        UserWithProfileResponse response = userProfileService.view(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/users/me/profile")
    @Operation(summary = "내 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않음 (CommonErrorCode.UNAUTHORIZED)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자/프로필/인바디 없음 (UserException.NOT_FOUND_LOGIN_ID, UserProfileException.NOT_FOUND, InBodyException.NOT_FOUND_PROFILE_ID)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserWithProfileResponse> getMyProfile() {
        UserWithProfileResponse response = userProfileService.viewMe();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/users/profiles/{profileId}/inbody-statistics")
    @Operation(summary = "인바디 통계 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<InBodyStatisticsResponse> getInBodyStatics(
            @Parameter(description = "프로필 ID", required = true)
            @PathVariable long profileId) {
        InBodyStatisticsResponse response = userProfileService.viewInBodyStatics(profileId);
        return ResponseEntity.ok(response);
    }
}
