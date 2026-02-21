package app.fitsync.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "로그인 ID", example = "fitsync_user01")
        @NotBlank
        @Size(max = 50)
        String loginId,

        @Schema(description = "비밀번호", example = "P@ssw0rd!")
        @NotBlank
        @Size(max = 100)
        String password
) {
}
