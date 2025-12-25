package app.fitsync.domain.profile.service;

import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserProfileResponse;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface UserProfileServiceInterface {

    UserProfileResponse create(UserProfileRequest request);
}
