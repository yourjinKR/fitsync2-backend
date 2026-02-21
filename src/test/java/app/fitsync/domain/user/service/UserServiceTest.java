package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.CurrentUserProvider;
import app.fitsync.domain.user.dto.UserDeleteRequest;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.entity.UserRoleType;
import app.fitsync.domain.user.mapper.UserMapper;
import app.fitsync.domain.user.oauth.SocialUserInfoExtractorRegistry;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.DeleteType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

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
    @DisplayName("TS-USER-003: HARD 삭제는 본인일 때 허용된다")
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
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(user.getLoginId()).thenReturn("owner");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "owner",
                        "N/A",
                        List.of(new SimpleGrantedAuthority("ROLE_MEMBER"))
                )
        );

        userService.deleteUser(new UserDeleteRequest(1L, DeleteType.HARD));

        verify(userRepository).delete(user);
        verify(jwtService).removeRefreshUser("owner");
    }

    @Test
    @DisplayName("TS-USER-003: HARD 삭제는 ADMIN일 때 허용된다")
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
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(user.getLoginId()).thenReturn("target-user");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "admin-user",
                        "N/A",
                        List.of(new SimpleGrantedAuthority("ROLE_" + UserRoleType.ADMIN.name()))
                )
        );

        userService.deleteUser(new UserDeleteRequest(2L, DeleteType.HARD));

        verify(userRepository).delete(user);
        verify(jwtService).removeRefreshUser("target-user");
    }

    @Test
    @DisplayName("TS-USER-003: HARD 삭제는 본인도 ADMIN도 아니면 거부된다")
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
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(user.getLoginId()).thenReturn("target-user");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "other-user",
                        "N/A",
                        List.of(new SimpleGrantedAuthority("ROLE_MEMBER"))
                )
        );

        assertThatThrownBy(() -> userService.deleteUser(new UserDeleteRequest(3L, DeleteType.HARD)))
                .isInstanceOf(AccessDeniedException.class);

        verify(userRepository, never()).delete(user);
        verify(jwtService, never()).removeRefreshUser("target-user");
    }
}

