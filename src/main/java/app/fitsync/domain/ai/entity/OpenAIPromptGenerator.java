package app.fitsync.domain.ai.entity;

import app.fitsync.domain.ai.dto.RoutineRecommendUserMessage;
import app.fitsync.domain.ai.dto.SystemPromptConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

@Getter
public class OpenAIPromptGenerator {

    private final UserMessage userMessage;
    private final SystemMessage systemMessage;
    private final AssistantMessage assistantMessage;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public OpenAIPromptGenerator(UserMessage userMessage, SystemMessage systemMessage, AssistantMessage assistantMessage) {
        this.userMessage = userMessage;
        this.systemMessage = systemMessage;
        this.assistantMessage = assistantMessage;
    }

    public static OpenAIPromptGenerator routineRecommendOf(RoutineRecommendUserMessage request)
            throws JsonProcessingException {

        UserMessage userMessage = new UserMessage(OBJECT_MAPPER.writeValueAsString(request));

        SystemMessage systemMessage = new SystemMessage(SystemPromptConstant.ROUTINE_REQUEST_JSON_SCHEMA);

        AssistantMessage assistantMessage = new AssistantMessage("");

        return new OpenAIPromptGenerator(userMessage, systemMessage, assistantMessage);
    }

    public List<Message> getListOf() {
        return List.of(this.userMessage, this.systemMessage, this.assistantMessage);
    }

    public Map<String, Object> getInputJsonOf() throws JsonProcessingException {
        Map<String, Object> inputJson = new HashMap<>();
        inputJson.put("systemPrompt", this.systemMessage.getText());

        inputJson.put("userMessage", OBJECT_MAPPER.readValue(
                this.userMessage.getText(),
                new TypeReference<Map<String, Object>>() {}
        ));

        inputJson.put("inputAssistantMessage", this.assistantMessage.getText());
        return inputJson;
    }
}
