package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.entity.AILog;
import app.fitsync.domain.ai.entity.AIModel;
import app.fitsync.domain.ai.entity.CallStatus;
import app.fitsync.domain.ai.exception.AILogErrorCode;
import app.fitsync.domain.ai.repository.AILogRepository;
import app.fitsync.global.exception.RestApiException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AILogWriter {

    private final AILogRepository repository;

    @Transactional
    public void init(
            String requestId,
            Long userId,
            AIModel model,
            String feature,
            String version,
            Map<String, Object> inputContent) {

        AILog log = AILog.builder()
                .requestId(requestId)
                .userId(userId)
                .model(model)
                .feature(feature)
                .version(version)
                .inputContent(inputContent)
                .requestTime(LocalDateTime.now())
                .status(CallStatus.SUCCESS)
                .statusMessage("pending")
                .build();

        repository.save(log);
    }

    @Transactional
    public void success(String requestId,
                        Map<String, Object> outputContent,
                        Long inputTokens,
                        Long outputTokens) {

        AILog log = repository.findByRequestId(requestId)
                .orElseThrow(() -> new RestApiException(AILogErrorCode.UUID_NOT_FOUND, requestId));

        LocalDateTime now = LocalDateTime.now();
        log.success(now, inputTokens, outputTokens);

        log.getOutputContent().putAll(outputContent == null ? Map.of() : outputContent);
    }

    @Transactional
    public void failure(
            String requestId,
            Long inputTokens,
            String errorMessage) {

        AILog log = repository.findByRequestId(requestId)
                .orElseThrow(() -> new RestApiException(AILogErrorCode.UUID_NOT_FOUND, requestId));

        LocalDateTime now = LocalDateTime.now();
        log.fail(now, errorMessage, inputTokens);
    }

}
