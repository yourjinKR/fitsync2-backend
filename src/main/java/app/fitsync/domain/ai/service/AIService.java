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
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService implements AIServiceInterface {

    private final ExerciseRepository exerciseRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final ExerciseMapper exerciseMapper;
    private final AILogWriter aiLogWriter;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatClient chatClient;

    private static final String MODEL = "gpt-4.1-mini";

    /*
    TODO:
     - 외부로부터 request를 직접 받는 것이 아닌, 실제 서비스는 내부적으로 사용자 정보를 찾은 후 조회해야 함.
     - 요청/응답 로깅 도메인 설계 및 적용
     */
    @Override
    public List<AIRoutineResponse> generateRoutine(AIRoutineRequest request) {

        String requestId = UUID.randomUUID().toString();

        long userId = request.userId();

        Integer splitCount = request.splitCount();

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RestApiException(UserProfileException.NOT_FOUND, userId));

        UserHeaderInfoResponse infoDto = userProfileMapper.toDto(profile.getUser());
        UserProfileDetailResponse profileDto = userProfileMapper.toDetailDto(profile);

        String inputUserMessage = MessageFormat.format("내 정보 : {0}, 내 프로필 : {1}, 루틴 분할 수 {2}", infoDto, profileDto, splitCount);
        String inputSystemMessage = SystemPromptConstant.ROUTINE_REQUEST;
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

        try {
            OpenAiChatOptions options = OpenAiChatOptions.builder()
                    .model(AIModel.GPT_4_1_MINI.getName())
                    .temperature(0.7)
                    .build();

            Prompt prompt = new Prompt(List.of(systemMessage, userMessage, assistantMessage), options);

            List<AIRoutineResponse> result = chatClient.prompt(prompt)
                    .tools(new AITools(exerciseRepository, exerciseMapper))
                    .call()
                    .entity(new ParameterizedTypeReference<>() {});

            Map<String, Object> outputJson = new HashMap<>();
            outputJson.put("result", result);

            aiLogWriter.success(
                    requestId,
                    outputJson,
                    null, // inputTokens (필요시 나중에 메타데이터에서 추출)
                    null  // outputTokens
            );

            return result;

        } catch (Exception e) {
            // 5) 실패 로그
            aiLogWriter.failure(requestId, null, e.getMessage());
            throw e;
        }
    }
}
