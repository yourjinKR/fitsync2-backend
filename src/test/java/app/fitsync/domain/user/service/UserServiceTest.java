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
import app.fitsync.global.DeleteType;
import app.fitsync.global.exception.RestApiException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
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
    @DisplayName("TS-USER-002: soft delete marks hidden and deletedAt")
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

        userService.deleteMe(DeleteType.SOFT);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.isHidden()).isTrue();
        assertThat(saved.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("TS-USER-003: hard delete allowed for owner")
    void deleteUser_hardDelete_allowedForOwner() {
        UserService userService = new UserService(
                passwordEncoder,
                userRepository,
                userMapper,
                jwtService,
                extractorRegistry,
                currentUserProvider
        );

        User user = org.mockito.Mockito.mock(User.class);
        when(currentUserProvider.getUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(user.getLoginId()).thenReturn("owner");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "owner",
                        "N/A",
                        List.of(new SimpleGrantedAuthority("ROLE_MEMBER"))
                )
        );

        userService.deleteMe(DeleteType.HARD);

        verify(userRepository).delete(user);
        verify(jwtService).removeRefreshUser("owner");
    }

    @Test
    @DisplayName("TS-USER-003: hard delete allowed for ADMIN")
    void deleteUser_hardDelete_allowedForAdmin() {
        UserService userService = new UserService(
                passwordEncoder,
                userRepository,
                userMapper,
                jwtService,
                extractorRegistry,
                currentUserProvider
        );

        User user = org.mockito.Mockito.mock(User.class);
        when(currentUserProvider.getUserId()).thenReturn(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(user.getLoginId()).thenReturn("target-user");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "admin-user",
                        "N/A",
                        List.of(new SimpleGrantedAuthority("ROLE_" + UserRoleType.ADMIN.name()))
                )
        );

        userService.deleteMe(DeleteType.HARD);

        verify(userRepository).delete(user);
        verify(jwtService).removeRefreshUser("target-user");
    }

    @Test
    @DisplayName("TS-USER-003: hard delete denied for non-owner and non-admin")
    void deleteUser_hardDelete_deniedForNonOwnerAndNonAdmin() {
        UserService userService = new UserService(
                passwordEncoder,
                userRepository,
                userMapper,
                jwtService,
                extractorRegistry,
                currentUserProvider
        );

        User user = org.mockito.Mockito.mock(User.class);
        when(currentUserProvider.getUserId()).thenReturn(3L);
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(user.getLoginId()).thenReturn("target-user");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "other-user",
                        "N/A",
                        List.of(new SimpleGrantedAuthority("ROLE_MEMBER"))
                )
        );

        assertThatThrownBy(() -> userService.deleteMe(DeleteType.HARD))
                .isInstanceOf(AccessDeniedException.class);

        verify(userRepository, never()).delete(user);
        verify(jwtService, never()).removeRefreshUser("target-user");
    }
}

