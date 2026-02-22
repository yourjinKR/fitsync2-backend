package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.CurrentUserProvider;
import app.fitsync.domain.user.dto.CustomOAuth2User;
import app.fitsync.domain.user.oauth.SocialUserInfo;
import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.dto.UserResponse;
import app.fitsync.domain.user.entity.SocialProviderType;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.entity.UserRoleType;
import app.fitsync.domain.user.exception.UserException;
import app.fitsync.domain.user.mapper.UserMapper;
import app.fitsync.domain.user.oauth.SocialUserInfoExtractor;
import app.fitsync.domain.user.oauth.SocialUserInfoExtractorRegistry;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService extends DefaultOAuth2UserService implements UserServiceInterface {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final SocialUserInfoExtractorRegistry extractorRegistry;
    private final CurrentUserProvider currentUserProvider;

    @Override
    public UserResponse createUser(UserRequest request) {

        String loginId = request.loginId();
        Boolean existed = existUser(loginId);

        if (existed)
            throw new RestApiException(UserException.DUPLICATE_LOGINID, loginId);

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = userMapper.toEntity(request, encodedPassword);
        User createdUser = userRepository.save(user);

        Long userId = createdUser.getId();
        return new UserResponse(userId);
    }

    @Override
    public Boolean existUser(String logiId) {
        return userRepository.existsByLoginId(logiId);
    }

    @Override
    public UserResponse findById(long id) {
        User user = findUserById(id);
        return new UserResponse(user.getId());
    }

    // User Soft Delete - 기본값
    @Override
    @Transactional
    public void deleteMe() {
        Long userId = currentUserProvider.getUserId();
        User user = findUserById(userId);
        user.hide();
        userRepository.save(user);
        jwtService.removeRefreshUser(user.getLoginId());
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RestApiException(UserException.NOT_FOUND, id));
    }

    @Override
    public UserResponse findMe() {
        Long userId = currentUserProvider.getUserId();
        return new UserResponse(userId);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        SocialProviderType socialProviderType = SocialProviderType.valueOf(registrationId);

        SocialUserInfoExtractor extractor = extractorRegistry.get(socialProviderType);
        SocialUserInfo info = extractor.extract(oAuth2User);

        User user = upsertSocialUser(info);

        String role = user.getRoleType() != null ? user.getRoleType().name() : UserRoleType.MEMBER.name();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        return new CustomOAuth2User(info.rawAttributes(), authorities, user.getLoginId());
    }

    private User upsertSocialUser(SocialUserInfo info) {
        String loginId = info.loginId();

        Optional<User> found = userRepository.findByLoginIdAndIsSocial(loginId, true);

        if (found.isPresent()) {
            User user = found.get();
            boolean changed = false;

            if (info.email() != null && !info.email().isBlank()) {
                // user.setEmail(info.email());
                changed = true;
            }

            if (info.name() != null && !info.name().isBlank()) {
                // user.setName(info.name());
                changed = true;
            }

            return changed ? userRepository.save(user) : user;
        }

        User newUser = User.builder()
                .loginId(loginId)
                .password("")
                .name(info.name() != null ? info.name() : "Unknown")
                .roleType(UserRoleType.MEMBER)
                .email(info.email())
                .isSocial(true)
                .socialProviderType(info.provider())
                .build();

        return userRepository.save(newUser);
    }
}
