package app.fitsync.global.config;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.controller.UserController;
import app.fitsync.domain.user.service.UserServiceInterface;
import app.fitsync.domain.workout.controller.WorkoutController;
import app.fitsync.domain.workout.service.WorkoutServiceInterface;
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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {WorkoutController.class, UserController.class})
@Import(SecurityConfig.class)
class CommonSecurityRegressionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkoutServiceInterface workoutService;

    @MockitoBean
    private UserServiceInterface userService;

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
    @DisplayName("TS-COMMON-001: 인증 없는 보호 API 접근 시 401")
    void protectedApiWithoutAuthentication_returns401() throws Exception {
        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("TS-COMMON-002: 권한 없는 자원 접근 시 403")
    void resourceWithoutRequiredAuthority_returns403() throws Exception {
        mockMvc.perform(get("/api/users/me").with(user("tester").roles("USER")))
                .andExpect(status().isForbidden());

        verify(userService, never()).findMe();
    }
}

