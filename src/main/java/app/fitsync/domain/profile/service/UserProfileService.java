package app.fitsync.domain.profile.service;

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
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.exception.UserException;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@NullMarked
@Service
@RequiredArgsConstructor
public class UserProfileService implements UserProfileServiceInterface {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final InBodyRecordRepository inBodyRecordRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional
    public UserProfileResponse create(UserProfileRequest request) {

        long userId = request.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserException.NOT_FOUND));

        boolean profilePresent = userProfileRepository.findByUserId(userId).isPresent();

        if (profilePresent)
            throw new RestApiException(UserProfileException.DUPLICATE, userId);

        UserProfile profile = userProfileMapper.toEntity(request, user);

        UserProfile save = userProfileRepository.save(profile);
        return new UserProfileResponse(save.getId());
    }

    @Override
    public UserWithProfileResponse view(long userId) {

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RestApiException(UserProfileException.NOT_FOUND, userId));

        Long userProfileId = profile.getId();

        InBodyRecord inBodyRecord = inBodyRecordRepository.findTop1ByUserProfile_IdOrderByCreatedAtDesc(userProfileId)
                .orElseThrow(() -> new RestApiException(InBodyException.NOT_FOUND_PROFILE_ID, userProfileId));

        return userProfileMapper.toDto(profile, inBodyRecord);
    }
}
