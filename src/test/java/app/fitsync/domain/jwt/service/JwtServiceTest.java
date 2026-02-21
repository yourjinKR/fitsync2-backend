package app.fitsync.domain.jwt.service;

import app.fitsync.domain.jwt.domain.RefreshEntity;
import app.fitsync.domain.jwt.dto.JWTResponse;
import app.fitsync.domain.jwt.dto.RefreshRequest;
import app.fitsync.domain.jwt.repository.RefreshRepository;
import app.fitsync.global.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private RefreshRepository refreshRepository;

    @Test
    @DisplayName("무효화되어 저장소에 없는 refresh 토큰으로 재발급 요청 시 실패한다")
    void refreshRotate_failsWhenRefreshTokenNotWhitelisted() {
        JwtService jwtService = new JwtService(refreshRepository);

        String refreshToken = JwtUtil.createJWT("tester", "MEMBER", false);
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken(refreshToken);

        when(refreshRepository.existsByRefresh(refreshToken)).thenReturn(false);

        assertThatThrownBy(() -> jwtService.refreshRotate(request))
                .isInstanceOf(RuntimeException.class);

        verify(refreshRepository).existsByRefresh(refreshToken);
        verify(refreshRepository, never()).save(org.mockito.ArgumentMatchers.any(RefreshEntity.class));
    }

    @Test
    @DisplayName("유효하고 저장된 refresh 토큰이면 rotate를 수행하고 기존 토큰을 제거한다")
    void refreshRotate_rotatesRefreshTokenWhenTokenIsValid() {
        JwtService jwtService = new JwtService(refreshRepository);

        String refreshToken = JwtUtil.createJWT("tester", "MEMBER", false);
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken(refreshToken);

        when(refreshRepository.existsByRefresh(refreshToken)).thenReturn(true);

        JWTResponse response = jwtService.refreshRotate(request);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();

        verify(refreshRepository).deleteByRefresh(refreshToken);
        ArgumentCaptor<RefreshEntity> captor = ArgumentCaptor.forClass(RefreshEntity.class);
        verify(refreshRepository).save(captor.capture());
        assertThat(captor.getValue().getLoginId()).isEqualTo("tester");
    }
}
