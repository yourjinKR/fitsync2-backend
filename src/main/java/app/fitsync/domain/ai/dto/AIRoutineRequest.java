package app.fitsync.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <h3>AI 루틴 추천 서비스 요청</h3>
 * - 사용자 ID
 * - 요청 루틴 분할 수
 */
@Schema(description = "AI 루틴 추천 요청")
public record AIRoutineRequest(
        @Schema(description = "사용자 ID", example = "1")
        long userId,

        @Schema(description = "분할 루틴 수", example = "3")
        Integer splitCount
) {

}
