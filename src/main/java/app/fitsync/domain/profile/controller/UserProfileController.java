package app.fitsync.domain.profile.controller;

import app.fitsync.domain.profile.dto.InBodyRecordRequest;
import app.fitsync.domain.profile.dto.InBodyRecordResponse;
import app.fitsync.domain.profile.dto.InBodyStatisticsResponse;
import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserProfileResponse;
import app.fitsync.domain.profile.dto.UserWithProfileResponse;
import app.fitsync.domain.profile.service.UserProfileServiceInterface;
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
public class UserProfileController {

    private final UserProfileServiceInterface userProfileService;

    @PostMapping("/api/user/profile")
    public ResponseEntity<UserProfileResponse> createProfile(@RequestBody UserProfileRequest request) {

        UserProfileResponse response = userProfileService.create(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/user/profile/inbody")
    public ResponseEntity<InBodyRecordResponse> createInBody(@RequestBody InBodyRecordRequest request) {

        InBodyRecordResponse response = userProfileService.createInBody(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/user/profile/{userId}")
    public ResponseEntity<UserWithProfileResponse> getProfile(@PathVariable long userId) {

        UserWithProfileResponse response = userProfileService.view(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/user/profile/me")
    public ResponseEntity<UserWithProfileResponse> getMyProfile() {

        UserWithProfileResponse response = userProfileService.viewMe();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/user/profile/inbody/statistics/{profileId}")
    public ResponseEntity<InBodyStatisticsResponse> getInBodyStatics(@PathVariable long profileId) {

        InBodyStatisticsResponse response = userProfileService.viewInBodyStatics(profileId);
        return ResponseEntity.ok(response);
    }
}
