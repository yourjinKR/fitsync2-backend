package app.fitsync.domain.profile.service;

import app.fitsync.domain.profile.dto.InBodyRecordRequest;
import app.fitsync.domain.profile.dto.InBodyRecordResponse;
import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserProfileResponse;
import app.fitsync.domain.profile.dto.UserWithProfileResponse;
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
    UserWithProfileResponse view(long id);
    UserWithProfileResponse viewMe();
    InBodyRecordResponse createInBody(InBodyRecordRequest request);
}
