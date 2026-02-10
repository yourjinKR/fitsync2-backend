package app.fitsync.domain.profile.service;

import app.fitsync.domain.profile.dto.InBodyRecordRequest;
import app.fitsync.domain.profile.dto.InBodyRecordResponse;
import app.fitsync.domain.profile.dto.UserWithProfileResponse;
import app.fitsync.domain.profile.entity.InBodyRecord;
import app.fitsync.domain.profile.exception.InBodyException;
import app.fitsync.domain.profile.exception.UserProfileException;
import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserProfileResponse;
import app.fitsync.domain.profile.entity.UserProfile;
import app.fitsync.domain.profile.mapper.UserProfileMapper;
import app.fitsync.domain.profile.repository.InBodyRecordRepository;
import app.fitsync.domain.profile.repository.UserProfileRepository;
import app.fitsync.domain.user.CurrentUserProvider;
import app.fitsync.domain.user.entity.User;
import app.fitsync.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@NullMarked
@Service
@RequiredArgsConstructor
public class UserProfileService implements UserProfileServiceInterface {

    private final CurrentUserProvider currentUserProvider;
    private final UserProfileRepository userProfileRepository;
    private final InBodyRecordRepository inBodyRecordRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional
    public UserProfileResponse create(UserProfileRequest request) {

        User user = currentUserProvider.getUser();
        long userId = user.getId();

        boolean profilePresent = userProfileRepository.findByUserId(userId).isPresent();

        if (profilePresent)
            throw new RestApiException(UserProfileException.DUPLICATE, userId);

        UserProfile profile = userProfileMapper.toEntity(request, user);

        UserProfile save = userProfileRepository.save(profile);
        return new UserProfileResponse(save.getId());
    }

    @Override
    @Transactional
    public InBodyRecordResponse createInBody(InBodyRecordRequest request) {

        long userId = request.userId();
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RestApiException(UserProfileException.NOT_FOUND, userId));

        InBodyRecord inBodyRecord = userProfileMapper.toEntity(request, profile);
        InBodyRecord save = inBodyRecordRepository.save(inBodyRecord);

        return new InBodyRecordResponse(save.getId());
    }

    @Override
    public UserWithProfileResponse view(long userId) {
        return findByUserId(userId);
    }

    @Override
    public UserWithProfileResponse viewMe() {
        Long userId = currentUserProvider.getUserId();
        return findByUserId(userId);
    }

    public UserWithProfileResponse findByUserId(long userId) {

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RestApiException(UserProfileException.NOT_FOUND, userId));

        Long userProfileId = profile.getId();

        InBodyRecord inBodyRecord = inBodyRecordRepository.findTop1ByUserProfile_IdOrderByCreatedAtDesc(userProfileId)
                .orElseThrow(() -> new RestApiException(InBodyException.NOT_FOUND_PROFILE_ID, userProfileId));

        return userProfileMapper.toDto(profile, inBodyRecord);
    }
}
