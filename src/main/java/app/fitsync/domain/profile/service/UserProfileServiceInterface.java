package app.fitsync.domain.profile.service;

import app.fitsync.domain.profile.dto.UserProfileDetailResponse;
import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserProfileResponse;
import org.jspecify.annotations.NullMarked;

/*

create
view
update
delete
list
search

 */
@NullMarked
public interface UserProfileServiceInterface {

    UserProfileResponse create(UserProfileRequest request);
    UserProfileDetailResponse view(long id);
}
