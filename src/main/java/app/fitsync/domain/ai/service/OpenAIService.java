package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import app.fitsync.domain.ai.dto.SystemPromptConstant;
import app.fitsync.domain.ai.entity.AIModel;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.profile.dto.UserProfileDetailResponse;
import app.fitsync.domain.profile.entity.UserProfile;
import app.fitsync.domain.profile.exception.UserProfileException;
import app.fitsync.domain.profile.mapper.UserProfileMapper;
import app.fitsync.domain.profile.repository.UserProfileRepository;
import app.fitsync.domain.user.dto.UserHeaderInfoResponse;
import app.fitsync.global.exception.RestApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIService implements AIServiceInterface {

    private final ExerciseRepository exerciseRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final ExerciseMapper exerciseMapper;
    private final AILogWriter aiLogWriter;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatClient chatClient;

    @Override
    public AIRoutineResponse generateRoutine(AIRoutineRequest request) throws JsonProcessingException {

        String requestId = UUID.randomUUID().toString();

        // request로부터 요청값을 받고 user를 조회하여 프롬프트를 완성

        long userId = request.userId();

        Integer splitCount = request.splitCount();

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RestApiException(UserProfileException.NOT_FOUND, userId));

        UserHeaderInfoResponse infoDto = userProfileMapper.toDto(profile.getUser());
        UserProfileDetailResponse profileDto = userProfileMapper.toDetailDto(profile);

        String inputUserMessage = MessageFormat.format("내 정보 : {0}, 내 프로필 : {1}, 루틴 분할 수 {2}", infoDto, profileDto, splitCount);
        String inputSystemMessage = SystemPromptConstant.ROUTINE_REQUEST_JSON_SCHEMA;
        String inputAssistantMessage = "";

        SystemMessage systemMessage = new SystemMessage(inputSystemMessage);
        UserMessage userMessage = new UserMessage(inputUserMessage);
        AssistantMessage assistantMessage = new AssistantMessage(inputAssistantMessage);

        Map<String, Object> inputJson = new HashMap<>();
        inputJson.put("systemPrompt", inputSystemMessage);
        inputJson.put("userMessage", inputUserMessage);
        inputJson.put("inputAssistantMessage", inputAssistantMessage);

        aiLogWriter.init(
                requestId,
                userId,
                AIModel.GPT_4_1_MINI,
                "ROUTINE_RECOMMEND",
                "0.0.1",
                inputJson
        );

        Long inputTokens = null;
        Long outputTokens = null;

        try {
            OpenAiChatOptions options = OpenAiChatOptions.builder()
                    .model(AIModel.GPT_4_1_MINI.getName())
                    .temperature(0.7)
                    .responseFormat(
                            ResponseFormat.builder()
                                    .type(ResponseFormat.Type.JSON_OBJECT)
                                    .build()
                    )
                    .build();

            Prompt prompt = new Prompt(List.of(systemMessage, userMessage, assistantMessage), options);

            ChatResponse response = chatClient.prompt(prompt)
                    .tools(new AITools(exerciseRepository, exerciseMapper))
                    .call()
                    .chatResponse();

            assert response != null;
                Usage usage = response.getMetadata().getUsage();

            inputTokens  = Long.valueOf(usage.getPromptTokens());
            outputTokens = Long.valueOf(usage.getCompletionTokens());

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
}
