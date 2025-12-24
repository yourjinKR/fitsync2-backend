package app.fitsync.domain.profile.service;

import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserProfileResponse;
import app.fitsync.domain.profile.entity.UserProfile;
import app.fitsync.domain.profile.mapper.UserProfileMapper;
import app.fitsync.domain.profile.repository.UserProfileRepository;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.repository.UserRepository;
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
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional
    public UserProfileResponse create(UserProfileRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(IllegalArgumentException::new);

        UserProfile profile = userProfileMapper.toEntity(request, user);
        UserProfile save = userProfileRepository.save(profile);

        return new UserProfileResponse(save.getId());
    }
}
