package app.fitsync.domain.ai.entity;

import app.fitsync.domain.ai.dto.SystemPromptConstant;
import app.fitsync.domain.profile.dto.UserProfileDetailResponse;
import app.fitsync.domain.user.dto.UserHeaderInfoResponse;
import java.text.MessageFormat;
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

    public OpenAIPromptGenerator(UserMessage userMessage, SystemMessage systemMessage, AssistantMessage assistantMessage) {
        this.userMessage = userMessage;
        this.systemMessage = systemMessage;
        this.assistantMessage = assistantMessage;
    }

    public static OpenAIPromptGenerator routineRecommendOf(UserHeaderInfoResponse infoDto, UserProfileDetailResponse profileDto, Integer splitCount) {
        UserMessage userMessage = new UserMessage(
                MessageFormat.format(
                        "내 정보 : {0}, 내 프로필 : {1}, 루틴 분할 수 {2}",
                        infoDto,
                        profileDto,
                        splitCount)
        );

        SystemMessage systemMessage = new SystemMessage(SystemPromptConstant.ROUTINE_REQUEST_JSON_SCHEMA);

        AssistantMessage assistantMessage = new AssistantMessage("");

        return new OpenAIPromptGenerator(userMessage, systemMessage, assistantMessage);
    }

    public List<Message> getListOf() {
        return List.of(this.userMessage, this.systemMessage, this.assistantMessage);
    }

    public Map<String, Object> getInputJsonOf() {
        Map<String, Object> inputJson = new HashMap<>();
        inputJson.put("systemPrompt", this.systemMessage);
        inputJson.put("userMessage", this.userMessage);
        inputJson.put("inputAssistantMessage", this.assistantMessage);
        return inputJson;
    }
}
