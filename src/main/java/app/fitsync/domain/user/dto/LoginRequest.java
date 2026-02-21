package app.fitsync.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "로그인 ID", example = "fitsync_user01")
        String loginId,
        @Schema(description = "비밀번호", example = "P@ssw0rd!")
        String password
) {
}
