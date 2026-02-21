package app.fitsync.domain.user.dto;

import app.fitsync.domain.user.entity.SocialProviderType;
import app.fitsync.domain.user.entity.UserRoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "회원 생성 요청")
public record UserRequest(
        @Schema(description = "로그인 ID", example = "fitsync_user01")
        @NotBlank
        @Size(max = 50)
        String loginId,

        @Schema(description = "비밀번호", example = "P@ssw0rd!")
        @NotBlank
        @Size(max = 100)
        String password,

        @Schema(description = "이름", example = "홍길동")
        @NotBlank
        @Size(max = 50)
        String name,

        @Schema(description = "권한", example = "MEMBER")
        @NotNull
        UserRoleType roleType,

        @Schema(description = "이메일", example = "user@example.com")
        @Email
        @NotBlank
        @Size(max = 100)
        String email,

        @Schema(description = "소셜 가입 여부", example = "false")
        @NotNull
        Boolean isSocial,

        @Schema(description = "소셜 제공자", example = "GOOGLE")
        SocialProviderType socialProviderType
) {
}
