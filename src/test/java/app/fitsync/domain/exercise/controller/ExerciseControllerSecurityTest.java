package app.fitsync.domain.exercise.controller;

import app.fitsync.domain.exercise.service.ExerciseServiceInterface;
import app.fitsync.domain.jwt.service.JwtService;
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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExerciseController.class)
@Import(SecurityConfig.class)
class ExerciseControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExerciseServiceInterface exerciseService;

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
    @DisplayName("TS-EX-SEC-001: 인증 없이 운동 세부 부위 목록 조회 시 401")
    void getBodyDetailPartList_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(get("/api/exercises/body-detail-parts"))
                .andExpect(status().isUnauthorized());

        verify(exerciseService, never()).getBodyDetailPartList();
    }

    @Test
    @DisplayName("TS-EX-SEC-002: 인증된 운동 세부 부위 목록 조회 시 200")
    void getBodyDetailPartList_withAuthentication_returns200() throws Exception {
        mockMvc.perform(get("/api/exercises/body-detail-parts").with(user("tester")))
                .andExpect(status().isOk());

        verify(exerciseService).getBodyDetailPartList();
    }
}

