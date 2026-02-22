package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.CurrentUserProvider;
import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.entity.UserRoleType;
import app.fitsync.domain.user.exception.UserException;
import app.fitsync.domain.user.mapper.UserMapper;
import app.fitsync.domain.user.oauth.SocialUserInfoExtractorRegistry;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private SocialUserInfoExtractorRegistry extractorRegistry;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("TS-USER-001: duplicate loginId signup fails")
    void createUser_duplicateLoginId_throwsDuplicate() {
        UserService userService = new UserService(
                passwordEncoder,
                userRepository,
                userMapper,
                jwtService,
                extractorRegistry,
                currentUserProvider
        );

        UserRequest request = new UserRequest(
                "duplicated",
                "password",
                "tester",
                UserRoleType.MEMBER,
                "user@example.com",
                false,
                null
        );
        when(userRepository.existsByLoginId("duplicated")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(UserException.DUPLICATE_LOGINID);

        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("TS-USER-002: delete me always performs SOFT delete and removes refresh token")
    void deleteUser_softDelete_setsHiddenAndDeletedAt() {
        UserService userService = new UserService(
                passwordEncoder,
                userRepository,
                userMapper,
                jwtService,
                extractorRegistry,
                currentUserProvider
        );

        User user = User.builder()
                .loginId("target-user")
                .password("pwd")
                .name("name")
                .roleType(UserRoleType.MEMBER)
                .email("u@test.com")
                .isSocial(false)
                .hidden(false)
                .build();

        when(currentUserProvider.getUserId()).thenReturn(10L);
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        userService.deleteMe();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.isHidden()).isTrue();
        assertThat(saved.getDeletedAt()).isNotNull();
        verify(jwtService).removeRefreshUser("target-user");
    }
}

