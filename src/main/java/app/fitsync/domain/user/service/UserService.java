package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.CurrentUserProvider;
import app.fitsync.domain.user.dto.CustomOAuth2User;
import app.fitsync.domain.user.oauth.SocialUserInfo;
import app.fitsync.domain.user.dto.UserDeleteRequest;
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
import app.fitsync.global.DeleteType;
import app.fitsync.global.exception.RestApiException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // User Soft Delete - 기본값
    @Override
    @Transactional
    public UserResponse deleteUser(UserDeleteRequest request) {
        Long userId = request.id();
        User user = findById(userId);
        DeleteType deleteType = request.deleteType();

        if (deleteType == DeleteType.SOFT) {
            user.hide();
            userRepository.save(user);
        }

        if (deleteType == DeleteType.HARD) {
            removeUser(user);
        }

        return new UserResponse(userId);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    // User Hard Delete
    public void removeUser(User user) throws AccessDeniedException {

        SecurityContext context = SecurityContextHolder.getContext();
        String sessionUsername = context.getAuthentication().getName();
        String sessionRole = context.getAuthentication().getAuthorities().iterator().next().getAuthority();

        String loginId = user.getLoginId();

        boolean isOwner = sessionUsername.equals(loginId);
        boolean isAdmin = sessionRole.equals("ROLE_"+ UserRoleType.ADMIN.name());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("본인 혹은 관리자만 삭제할 수 있습니다.");
        }

        userRepository.delete(user);
        jwtService.removeRefreshUser(loginId);
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
