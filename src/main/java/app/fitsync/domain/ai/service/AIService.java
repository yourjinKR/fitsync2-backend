package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import app.fitsync.domain.ai.dto.SystemPromptConstant;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import java.text.MessageFormat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService implements AIServiceInterface {

    private final OpenAiChatModel openAiChatModel;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    private static final String MODEL = "gpt-4.1-mini";

    @Override
    public String generateTest(String text) {

        ChatClient chatClient = ChatClient.create(openAiChatModel);

        SystemMessage systemMessage = new SystemMessage("");
        UserMessage userMessage = new UserMessage(text);
        AssistantMessage assistantMessage = new AssistantMessage("");

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(MODEL)
                .temperature(0.7)
                .build();

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage, assistantMessage), options);

        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    /*
    TODO:
     - 외부로부터 request를 직접 받는 것이 아닌, 실제 서비스는 내부적으로 사용자 정보를 찾은 후 조회해야 함.
     - 요청/응답 로깅 도메인 설계 및 적용
     */
    @Override
    public List<AIRoutineResponse> generateRoutine(AIRoutineRequest request) {

        ChatClient chatClient = ChatClient.create(openAiChatModel);

        SystemMessage systemMessage = new SystemMessage(SystemPromptConstant.ROUTINE_REQUEST);
        UserMessage userMessage = new UserMessage(MessageFormat.format("내 정보 : {0}", request.toString()));
        AssistantMessage assistantMessage = new AssistantMessage("");

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(MODEL)
                .temperature(0.7)
                .build();

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage, assistantMessage), options);

        return chatClient.prompt(prompt)
                .tools(new AITools(exerciseRepository, exerciseMapper))
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }
}
