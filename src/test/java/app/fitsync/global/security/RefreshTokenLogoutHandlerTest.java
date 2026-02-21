package app.fitsync.global.security;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RefreshTokenLogoutHandlerTest {

    @Mock
    private JwtService jwtService;

    @Test
    @DisplayName("요청 바디가 비어있으면 refresh 토큰 삭제를 수행하지 않는다")
    void logout_skipsWhenBodyIsEmpty() {
        RefreshTokenLogoutHandler handler = new RefreshTokenLogoutHandler(jwtService);
        HttpServletRequest request = new MockHttpServletRequest();

        handler.logout(request, new MockHttpServletResponse(), null);

        verify(jwtService, never()).removeRefresh(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("유효한 refresh 토큰이 있으면 해당 토큰을 삭제한다")
    void logout_removesRefreshTokenWhenTokenIsValid() {
        RefreshTokenLogoutHandler handler = new RefreshTokenLogoutHandler(jwtService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setContent("{\"refreshToken\":\"sample.refresh.token\"}".getBytes());

        try (MockedStatic<JwtUtil> mocked = org.mockito.Mockito.mockStatic(JwtUtil.class)) {
            mocked.when(() -> JwtUtil.isValid("sample.refresh.token", false)).thenReturn(true);

            handler.logout(request, new MockHttpServletResponse(), null);

            verify(jwtService).removeRefresh("sample.refresh.token");
        }
    }

    @Test
    @DisplayName("refresh 토큰이 유효하지 않으면 삭제를 수행하지 않는다")
    void logout_skipsWhenTokenIsInvalid() {
        RefreshTokenLogoutHandler handler = new RefreshTokenLogoutHandler(jwtService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setContent("{\"refreshToken\":\"invalid.token\"}".getBytes());

        try (MockedStatic<JwtUtil> mocked = org.mockito.Mockito.mockStatic(JwtUtil.class)) {
            mocked.when(() -> JwtUtil.isValid("invalid.token", false)).thenReturn(false);

            handler.logout(request, new MockHttpServletResponse(), null);

            verify(jwtService, never()).removeRefresh(org.mockito.ArgumentMatchers.anyString());
        }
    }
}

