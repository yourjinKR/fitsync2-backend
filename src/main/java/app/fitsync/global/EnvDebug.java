package app.fitsync.global;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvDebug implements CommandLineRunner {

    private final Environment env;

    public EnvDebug(Environment env) {
        this.env = env;
    }

    @Override
    public void run(String... args) {
        System.out.println("OPEN_AI_SECRET_KEY = " + env.getProperty("OPEN_AI_SECRET_KEY"));
        System.out.println(env.getProperty("NAVER_CLIENT_ID"));
        System.out.println(env.getProperty("NAVER_SECRET_KEY"));
        System.out.println(env.getProperty("GOOGLE_CLIENT_ID"));
        System.out.println(env.getProperty("GOOGLE_SECRET_KEY"));

        System.out.println("spring.ai.openai.api-key = " +
                env.getProperty("spring.ai.openai.api-key"));
    }
}
