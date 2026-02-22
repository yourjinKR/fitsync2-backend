package app.fitsync.domain.profile.service;

import app.fitsync.domain.profile.dto.InBodyRecordResponse;
import app.fitsync.domain.profile.dto.InBodyRecordDetailResponse;
import app.fitsync.domain.profile.dto.InBodyStatisticsResponse;
import app.fitsync.domain.profile.dto.InBodyRecordMeRequest;
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
    InBodyRecordResponse createMyInBody(InBodyRecordMeRequest request);
    InBodyRecordDetailResponse viewMyInBodyRecord(long inBodyRecordId);
    UserWithProfileResponse view(long id);
    UserWithProfileResponse viewMe();
    InBodyStatisticsResponse viewInBodyStatics(long profileId);
}
