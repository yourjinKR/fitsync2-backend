package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AILogResponse;
import app.fitsync.domain.ai.entity.AILog;
import app.fitsync.domain.ai.exception.AILogErrorCode;
import app.fitsync.domain.ai.mapper.AILogMapper;
import app.fitsync.domain.ai.repository.AILogRepository;
import app.fitsync.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AILogServiceTest {

    @Mock
    private AILogRepository aiLogRepository;

    @Mock
    private AILogMapper aiLogMapper;

    @Test
    @DisplayName("TS-AI-005: AI 로그 상세 조회 성공")
    void viewDetail_success() {
        AILogService service = new AILogService(aiLogRepository, aiLogMapper);
        AILog log = AILog.builder().id(1L).requestId("req").build();
        AILogResponse dto = new AILogResponse(
                1L, "req", 1L, null, "ROUTINE_RECOMMEND", "0.0.1",
                Map.of(), Map.of(), 10L, 20L, LocalDateTime.now(), LocalDateTime.now(),
                100L, null, null, null, null, null
        );

        when(aiLogRepository.findById(1L)).thenReturn(Optional.of(log));
        when(aiLogMapper.toDto(log)).thenReturn(dto);

        AILogResponse response = service.viewDetail(1L);
        assertThat(response.id()).isEqualTo(1L);
        verify(aiLogMapper).toDto(log);
    }

    @Test
    @DisplayName("TS-AI-005: 없는 AI 로그 ID 조회 시 NOT_FOUND")
    void viewDetail_notFound_throwsNotFound() {
        AILogService service = new AILogService(aiLogRepository, aiLogMapper);
        when(aiLogRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.viewDetail(999L))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(AILogErrorCode.NOT_FOUND);
    }
}

