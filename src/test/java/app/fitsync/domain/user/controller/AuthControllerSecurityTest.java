package app.fitsync.domain.user.controller;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.service.AuthServiceInterface;
import app.fitsync.global.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthServiceInterface authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationConfiguration authenticationConfiguration;

    @MockitoBean(name = "LoginSuccessHandler")
    private AuthenticationSuccessHandler loginSuccessHandler;

    @MockitoBean(name = "SocialSuccessHandler")
    private AuthenticationSuccessHandler socialSuccessHandler;

    @BeforeEach
    void setUp() throws Exception {
        AuthenticationManager authenticationManager = org.mockito.Mockito.mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);
    }

    @Test
    @DisplayName("TS-AUTH-004: 인증 없이 로그아웃 호출 시 401")
    void logout_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("TS-AUTH-005: 인증된 로그아웃 호출 시 200 및 서비스 호출")
    void logout_withAuthentication_returns200AndCallsService() throws Exception {
        mockMvc.perform(post("/api/auth/logout").with(user("tester")))
                .andExpect(status().isOk());

        verify(authService).logout();
    }
}
