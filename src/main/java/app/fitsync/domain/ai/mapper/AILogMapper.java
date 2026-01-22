package app.fitsync.domain.ai.mapper;

import app.fitsync.domain.ai.dto.AILogResponse;
import app.fitsync.domain.ai.entity.AILog;
import org.springframework.stereotype.Component;

@Component
public class AILogMapper {

    public AILogResponse toDto(AILog log) {

        return new AILogResponse(
                log.getId(),
                log.getRequestId(),
                log.getUserId(),
                log.getModel(),
                log.getFeature(),
                log.getVersion(),
                log.getInputContent(),
                log.getOutputContent(),
                log.getInputTokens(),
                log.getOutputTokens(),
                log.getRequestTime(),
                log.getResponseTime(),
                log.getElapsedMs(),
                log.getStatus(),
                log.getStatusMessage(),
                log.getUserFeedBack(),
                log.getUserFeedBackReason(),
                log.getUserAction()
        );
    }
}
