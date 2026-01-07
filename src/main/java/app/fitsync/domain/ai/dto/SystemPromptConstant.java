package app.fitsync.domain.ai.dto;

public class SystemPromptConstant {
    public static final String ROUTINE_REQUEST = """
            # 요구사항
            - DB 상에 있는 운동정보 내에서만 추천 할 것
            - 유저 메세지를 참고할 것
            """;
}
