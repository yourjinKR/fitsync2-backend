package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.dto.LoginRequest;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.entity.UserRoleType;
import app.fitsync.domain.user.exception.UserException;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("로그아웃 시 SecurityContext의 loginId 기준으로 refresh 토큰을 무효화한다")
    void logout_removesRefreshTokensForAuthenticatedUser() {
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("tester", "N/A")
        );

        authService.logout();

        verify(jwtService).removeRefreshUser("tester");
    }

    @Test
    @DisplayName("인증 정보가 없으면 로그아웃 서비스는 예외를 던진다")
    void logout_withoutAuthentication_throwsException() {
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);

        assertThatThrownBy(authService::logout).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("TS-AUTH-001: 로그인 성공 시 access/refresh 토큰을 발급한다")
    void login_success_issuesAccessAndRefreshTokens() {
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);
        LoginRequest request = new LoginRequest("tester", "plain-password");

        User user = org.mockito.Mockito.mock(User.class);
        when(userRepository.findByLoginIdAndIsSocial("tester", false)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn("encoded-password");
        when(user.getRoleType()).thenReturn(UserRoleType.MEMBER);
        when(passwordEncoder.matches("plain-password", "encoded-password")).thenReturn(true);

        var response = authService.login(request);

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
        verify(jwtService).addRefresh("tester", response.refreshToken());
    }

    @Test
    @DisplayName("TS-AUTH-002: 비밀번호 불일치 시 INVALID_PASSWORD 예외를 반환한다")
    void login_invalidPassword_throwsInvalidPassword() {
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);
        LoginRequest request = new LoginRequest("tester", "wrong-password");

        User user = org.mockito.Mockito.mock(User.class);
        when(userRepository.findByLoginIdAndIsSocial("tester", false)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn("encoded-password");
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(UserException.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("TS-AUTH-003: 존재하지 않는 loginId 시 NOT_FOUND_LOGIN_ID 예외를 반환한다")
    void login_userNotFound_throwsNotFoundLoginId() {
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);
        LoginRequest request = new LoginRequest("missing-user", "password");

        when(userRepository.findByLoginIdAndIsSocial("missing-user", false)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(UserException.NOT_FOUND_LOGIN_ID);
    }
}

