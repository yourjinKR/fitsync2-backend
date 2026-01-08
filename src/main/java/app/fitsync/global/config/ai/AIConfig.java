package app.fitsync.global.config.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AIConfig {

    private final ChatModel openAIChatModel;

    @Bean
    public ChatClient chatClient() {
        return ChatClient.create(openAIChatModel);
    }
}
