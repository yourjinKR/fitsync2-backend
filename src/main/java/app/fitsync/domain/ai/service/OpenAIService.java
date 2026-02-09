package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import app.fitsync.domain.ai.dto.RoutineRecommendUserMessage;
import app.fitsync.domain.ai.entity.AIModel;
import app.fitsync.domain.ai.entity.OpenAiMessageConverter;
import app.fitsync.domain.profile.entity.InBodyRecord;
import app.fitsync.domain.profile.entity.UserProfile;
import app.fitsync.domain.profile.exception.InBodyException;
import app.fitsync.domain.profile.exception.UserProfileException;
import app.fitsync.domain.profile.mapper.UserProfileMapper;
import app.fitsync.domain.profile.repository.InBodyRecordRepository;
import app.fitsync.domain.profile.repository.UserProfileRepository;
import app.fitsync.global.exception.RestApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIService implements AIServiceInterface {

    private final UserProfileRepository userProfileRepository;
    private final InBodyRecordRepository inBodyRecordRepository;
    private final UserProfileMapper userProfileMapper;
    private final AILogWriter aiLogWriter;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatClient chatClient;
    private final AITools aiTools;

    /*

    TODO: 로직 관심사에 따른 분리 리팩토링 여부에 대해 고민 (프롬프트 생성, API 요청, 로깅)

    1. request로부터 요청에 필요한 값들을 받아 프로필 조회
    2. 유저 기록을 기반으로 프롬프트 생성 (유저 메세지)
    3. 메세지 객체 생성
    4. 로그 작성 (대기 상태)
    // try catch 시작
    5. 옵션 빌더를 통해 옵션 생성
    6. 응답 받은 ChatResponse 내부의 메타 데이터를 기반으로 추가 로그 정보 작성 // 성공시, 토큰

     */
    @Override
    public AIRoutineResponse generateRoutine(AIRoutineRequest request) throws JsonProcessingException {

        String requestId = UUID.randomUUID().toString();

        long userId = request.userId();

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RestApiException(UserProfileException.NOT_FOUND, userId));

        Long userProfileId = profile.getId();

        InBodyRecord inBodyRecord = inBodyRecordRepository.findTop1ByUserProfile_IdOrderByCreatedAtDesc(userProfileId)
                .orElseThrow(() -> new RestApiException(InBodyException.NOT_FOUND_PROFILE_ID, userProfileId));

        RoutineRecommendUserMessage userMessageRequest = userProfileMapper.toDto(profile, inBodyRecord, request);
        OpenAiMessageConverter openAiMessageConverter = OpenAiMessageConverter.routineRecommendOf(userMessageRequest);

        aiLogWriter.init(
                requestId,
                userId,
                AIModel.GPT_4_1_MINI,
                "ROUTINE_RECOMMEND",
                "0.0.1",
                openAiMessageConverter.getInputJsonOf()
        );

        Prompt prompt = getRoutineRecommendPrompt(openAiMessageConverter);

        Long inputTokens = null;

        try {
            ChatResponse response = chatClient.prompt(prompt)
                    .tools(aiTools)
                    .call()
                    .chatResponse();

            assert response != null;
                Usage usage = response.getMetadata().getUsage();

            inputTokens  = Long.valueOf(usage.getPromptTokens());
            Long outputTokens = Long.valueOf(usage.getCompletionTokens());

            String content = response.getResult().getOutput().getText();
            AIRoutineResponse result =
                    objectMapper.readValue(content, AIRoutineResponse.class);

            Map<String, Object> outputJson = new HashMap<>();
            outputJson.put("result", result);

            aiLogWriter.success(
                    requestId,
                    outputJson,
                    inputTokens,
                    outputTokens
            );

            return result;

        } catch (Exception e) {
            aiLogWriter.failure(requestId, inputTokens, e.getMessage());
            throw e;
        }
    }

    public Prompt getRoutineRecommendPrompt(OpenAiMessageConverter openAiMessageConverter) {

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(AIModel.GPT_4_1_MINI.getName())
                .temperature(0.7)
                .responseFormat(
                        ResponseFormat.builder()
                                .type(ResponseFormat.Type.JSON_OBJECT)
                                .build()
                )
                .build();

        return new Prompt(openAiMessageConverter.getListOf(), options);
    }
}
