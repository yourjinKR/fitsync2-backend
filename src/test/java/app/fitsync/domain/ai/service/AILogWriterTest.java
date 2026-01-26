package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.entity.AILog;
import app.fitsync.domain.ai.entity.AIModel;
import app.fitsync.domain.ai.repository.AILogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AILogWriterTest {

    @Mock
    private AILogRepository repository;

    private AILogWriter aiLogWriter;

    @BeforeEach
    void setUp() {
        aiLogWriter = new AILogWriter(repository);
    }

    @Test
    @DisplayName("초기화 시 AILog의 상태(status)와 상태 메세지(statusMessage)는 null로 저장된다")
    void initLog_statusAndStatusMessage_areNull() {
        // given
        String requestId = "test-request-id";
        Long userId = 1L;
        AIModel model = AIModel.GPT_4_1_MINI;
        String feature = "ROUTINE_RECOMMEND";
        String version = "0.0.1";
        Map<String, Object> inputContent = Map.of("key", "value");

        // when
        aiLogWriter.init(
                requestId,
                userId,
                model,
                feature,
                version,
                inputContent
        );

        // then
        ArgumentCaptor<AILog> logCaptor = ArgumentCaptor.forClass(AILog.class);
        verify(repository).save(logCaptor.capture());

        AILog savedLog = logCaptor.getValue();

        // 핵심 검증 포인트
        assertThat(savedLog.getStatus()).isNull();
        assertThat(savedLog.getStatusMessage()).isNull();

        // 부가 검증 (의도 명확화용, 선택)
        assertThat(savedLog.getRequestId()).isEqualTo(requestId);
        assertThat(savedLog.getUserId()).isEqualTo(userId);
        assertThat(savedLog.getModel()).isEqualTo(model);
        assertThat(savedLog.getFeature()).isEqualTo(feature);
        assertThat(savedLog.getVersion()).isEqualTo(version);
        assertThat(savedLog.getInputContent()).containsEntry("key", "value");
        assertThat(savedLog.getRequestTime()).isNotNull();
    }
}
