package app.fitsync.domain.ai.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService implements AIServiceInterface {

    private final OpenAiChatModel openAiChatModel;

    private static final String MODEL = "gpt-4.1-mini";

    @Override
    public String generate(String text) {

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
}
