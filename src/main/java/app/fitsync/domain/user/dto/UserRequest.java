package app.fitsync.domain.user.dto;

import app.fitsync.domain.user.entity.SocialProviderType;
import app.fitsync.domain.user.entity.UserRoleType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 생성 요청")
public record UserRequest(
        @Schema(description = "로그인 ID", example = "fitsync_user01")
        String loginId,
        @Schema(description = "비밀번호", example = "P@ssw0rd!")
        String password,
        @Schema(description = "이름", example = "홍길동")
        String name,
        @Schema(description = "권한", example = "MEMBER")
        UserRoleType roleType,
        @Schema(description = "이메일", example = "user@example.com")
        String email,
        @Schema(description = "소셜 가입 여부", example = "false")
        Boolean isSocial,
        @Schema(description = "소셜 제공자", example = "GOOGLE")
        SocialProviderType socialProviderType
) {

}
