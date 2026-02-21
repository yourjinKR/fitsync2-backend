package app.fitsync.domain.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인바디 기록 생성 응답")
public record InBodyRecordResponse(
        @Schema(description = "인바디 기록 ID", example = "100")
        long id
) {
}
