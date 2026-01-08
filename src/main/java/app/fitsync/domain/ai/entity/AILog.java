package app.fitsync.domain.ai.entity;


import app.fitsync.global.config.jpa.JsonMapConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "ai_logs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AILog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로깅용 UUID
    @Column(nullable = false, length = 64)
    private String requestId;

    // 요청자 id
    @Column(name = "user_id")
    private Long userId;

    // AI 모델
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private AIModel model;

    // 서비스 타입 (예시 : 루틴 추천, 운동 피드백, 맞춤형 챗봇 등등...)
    @Column(name = "feature", nullable = false, length = 64)
    private String feature;

    // 서비스 버전 (예시 : 0.0.1, 0.0.2 ...)
    @Column(length = 32)
    private String version;

    // 내용 전부 저장
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "input_content", columnDefinition = "json")
    @Builder.Default
    private Map<String, Object> inputContent = new HashMap<>();

    // 내용 전부 저장
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "output_content", columnDefinition = "json")
    @Builder.Default
    private Map<String, Object> outputContent = new HashMap<>();

    // 입력 토큰
    @Column(name = "input_tokens")
    private Long inputTokens;

    // 출력 토큰
    @Column(name = "output_tokens")
    private Long outputTokens;

    // 요청 시간
    @Column(name = "request_time")
    private LocalDateTime requestTime;

    // 응답 시간
    @Column(name = "response_time")
    private LocalDateTime responseTime;

    // 유저 시간
    @Column(name = "elapsed_ms")
    private Long elapsedMs;

    @Enumerated(EnumType.STRING)
    private CallStatus status;

    // 원인 (대표적으로 Exception의 경우 어떤 이유인지 기록)
    @Column(name = "status_message")
    private String statusMessage;

    // 유저 피드백 (LIKE or DISLIKE)
    @Enumerated(EnumType.STRING)
    @Column(name = "user_feedback")
    private FeedBackStatus userFeedBack;

    // 유저 피드백 사유 (DISLIKE일때 사용자로부터 이유를 수집함, '기타' 항목을 고려했기에 String)
    @Column(name = "user_feedback_reason")
    private String userFeedBackReason;

    // 응답 후 사용자의 행동 분석 (예시 : 추천 결과를 저장, 저장하지 않음 등등...)
    @Enumerated(EnumType.STRING)
    @Column(name = "user_action")
    private UserAction userAction;

    public void recordTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
        this.elapsedMs = Duration.between(this.requestTime, this.responseTime).toMillis();
    }

    public void setTokens(Long inputTokens, Long outputTokens) {
        if (inputTokens != null)
            this.inputTokens = inputTokens;
        if (outputTokens != null)
            this.outputTokens = outputTokens;
    }

    public void success(LocalDateTime responseTime, Long inputTokens, Long outputTokens) {
        this.status = CallStatus.SUCCESS;
        this.statusMessage = "OK";

        recordTime(responseTime);
        setTokens(inputTokens, outputTokens);
    }

    public void fail(LocalDateTime responseTime, String errorMessage, Long inputTokens) {
        this.status = CallStatus.FAILURE;
        this.statusMessage = errorMessage;

        recordTime(responseTime);

        if (inputTokens != null)
            this.inputTokens = inputTokens;
    }


}
