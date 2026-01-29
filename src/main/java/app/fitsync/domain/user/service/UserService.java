package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.dto.CustomOAuth2User;
import app.fitsync.domain.user.dto.UserDeleteRequest;
import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.dto.UserResponse;
import app.fitsync.domain.user.entity.SocialProviderType;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.entity.UserRoleType;
import app.fitsync.domain.user.exception.UserException;
import app.fitsync.domain.user.mapper.UserMapper;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.DeleteType;
import app.fitsync.global.exception.RestApiException;
import java.util.List;
import java.util.Map;
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

    // 자체/소셜 로그인 회원 탈퇴
    public void removeUser(User user) throws AccessDeniedException {

        // 본인 및 어드민만 삭제 가능 검증
        SecurityContext context = SecurityContextHolder.getContext();
        String sessionUsername = context.getAuthentication().getName();
        String sessionRole = context.getAuthentication().getAuthorities().iterator().next().getAuthority();

        String loginId = user.getLoginId();

        boolean isOwner = sessionUsername.equals(loginId);
        boolean isAdmin = sessionRole.equals("ROLE_"+ UserRoleType.ADMIN.name());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("본인 혹은 관리자만 삭제할 수 있습니다.");
        }

        // 유저 제거
        userRepository.delete(user);

        // Refresh 토큰 제거
        jwtService.removeRefreshUser(loginId);
    }

    public UserResponse findMe() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByLoginIdAndHiddenIsFalse(loginId)
                .orElseThrow(() -> new RestApiException(UserException.NOT_FOUND_LOGIN_ID, loginId));

        return new UserResponse(user.getId());
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 부모 메소드 호출
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 데이터
        Map<String, Object> attributes;
        List<GrantedAuthority> authorities;

        String loginId;
        String role = UserRoleType.MEMBER.name();
        String email;
        String name;

        // provider 제공자별 데이터 획득
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        if (registrationId.equals(SocialProviderType.NAVER.name())) {

            attributes = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            loginId = registrationId + "_" + attributes.get("id");
            email = attributes.get("email").toString();
            name = attributes.get("name").toString();

        } else if (registrationId.equals(SocialProviderType.GOOGLE.name())) {

            attributes = (Map<String, Object>) oAuth2User.getAttributes();
            loginId = registrationId + "_" + attributes.get("sub");
            email = attributes.get("email").toString();
            name = attributes.get("name").toString();

        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        // 데이터베이스 조회 -> 존재하면 업데이트, 없으면 신규 가입
        Optional<User> user = userRepository.findByLoginIdAndIsSocial(loginId, true);

        if (user.isPresent()) {
            // 기존 유저 업데이트 추가 예정

//            role = user.get().getRoleType().name();
//
//            UserRequestDTO dto = new UserRequestDTO();
//            dto.setNickname(name);
//            dto.setEmail(email);
//            user.get().updateUser(dto);
//
//            userRepository.save(user.get());
        } else {

            // 신규 가입
            User newUser = User.builder()
                    .loginId(loginId)
                    .password("")
                    .name(name)
                    .roleType(UserRoleType.MEMBER)
                    .email(email)
                    .isSocial(true)
                    .socialProviderType(SocialProviderType.valueOf(registrationId))
                    .build();

            userRepository.save(newUser);
        }

        authorities = List.of(new SimpleGrantedAuthority(role));

        return new CustomOAuth2User(attributes, authorities, loginId);
    }
}
