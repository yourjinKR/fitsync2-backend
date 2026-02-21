package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

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
}

