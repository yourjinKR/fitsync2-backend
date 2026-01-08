package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import app.fitsync.domain.ai.dto.SystemPromptConstant;
import app.fitsync.domain.ai.entity.AIModel;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
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
        Map<String, Object> inputJson = new HashMap<>();
        inputJson.put("systemPrompt", "ROUTINE_REQUEST");
        inputJson.put("userInfoPreview", request.toString()); // 필요 시 마스킹/요약 적용
        inputJson.put("model", MODEL);

        aiLogWriter.init(
                requestId,
                null, // 임시
                AIModel.GPT_4_1_MINI,
                "ROUTINE_RECOMMEND",
                "0.0.1",
                inputJson
        );

        try {
            SystemMessage systemMessage = new SystemMessage(SystemPromptConstant.ROUTINE_REQUEST);
            UserMessage userMessage = new UserMessage(MessageFormat.format("내 정보 : {0}", request.toString()));
            AssistantMessage assistantMessage = new AssistantMessage("");

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
