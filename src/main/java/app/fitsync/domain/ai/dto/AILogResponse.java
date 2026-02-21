package app.fitsync.domain.ai.dto;

import app.fitsync.domain.ai.entity.AIModel;
import app.fitsync.domain.ai.entity.CallStatus;
import app.fitsync.domain.ai.entity.FeedBackStatus;
import app.fitsync.domain.ai.entity.UserAction;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "AI log detail response")
public record AILogResponse(
        Long id,
        String requestId,
        Long userId,
        AIModel model,
        String feature,
        String version,
        Map<String, Object> inputContent,
        Map<String, Object> outputContent,
        Long inputTokens,
        Long outputTokens,
        LocalDateTime requestTime,
        LocalDateTime responseTime,
        Long elapsedMs,
        CallStatus status,
        String statusMessage,
        FeedBackStatus userFeedBack,
        String userFeedBackReason,
        UserAction userAction
) {
}
