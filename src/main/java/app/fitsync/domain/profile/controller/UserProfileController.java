package app.fitsync.domain.profile.controller;

import app.fitsync.domain.profile.dto.UserProfileDetailResponse;
import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserProfileResponse;
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
    public ResponseEntity<UserProfileResponse> create(@RequestBody UserProfileRequest request) {

        UserProfileResponse response = userProfileService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/user/profile/{userId}")
    public ResponseEntity<UserProfileDetailResponse> view(@PathVariable long userId) {

        UserProfileDetailResponse response = userProfileService.view(userId);
        return ResponseEntity.ok(response);
    }
}
