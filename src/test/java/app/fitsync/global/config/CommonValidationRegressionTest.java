package app.fitsync.global.config;

import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.controller.AuthController;
import app.fitsync.domain.user.service.AuthServiceInterface;
import app.fitsync.domain.workout.controller.WorkoutController;
import app.fitsync.domain.workout.service.WorkoutServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthController.class, WorkoutController.class})
@Import(SecurityConfig.class)
class CommonValidationRegressionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthServiceInterface authService;

    @MockitoBean
    private WorkoutServiceInterface workoutService;

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
    @DisplayName("TS-COMMON-003: 로그인 요청 검증 실패 시 INVALID_PARAMETER와 field errors를 반환한다")
    void loginValidationFailure_returnsInvalidParameterWithFieldErrors() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loginId\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.errors[*].field", hasItem("loginId")))
                .andExpect(jsonPath("$.errors[*].field", hasItem("password")));

        verify(authService, never()).login(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("TS-COMMON-003: 운동 생성 요청 검증 실패 시 INVALID_PARAMETER와 field errors를 반환한다")
    void workoutCreateValidationFailure_returnsInvalidParameterWithFieldErrors() throws Exception {
        mockMvc.perform(post("/api/workouts")
                        .with(user("tester"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "writerId": 0,
                                  "ownerId": 0,
                                  "memo": "test",
                                  "workoutExercises": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.errors[*].field", hasItem("writerId")))
                .andExpect(jsonPath("$.errors[*].field", hasItem("ownerId")))
                .andExpect(jsonPath("$.errors[*].field", hasItem("workoutExercises")));

        verify(workoutService, never()).create(org.mockito.ArgumentMatchers.any());
    }
}

