package app.fitsync.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "AI 루틴 추천 요청")
public record AIRoutineRequest(
        @Schema(description = "사용자 ID", example = "1")
        @Positive
        long userId,

        @Schema(description = "분할 루틴 수", example = "3")
        @NotNull
        @Min(1)
        @Max(7)
        Integer splitCount
) {
}
