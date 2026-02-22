package app.fitsync.domain.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "인바디 기록 단건 조회 응답")
public record InBodyRecordDetailResponse(
        @Schema(description = "인바디 기록 ID", example = "100")
        long id,

        @Schema(description = "프로필 ID", example = "10")
        long profileId,

        @Schema(description = "체중", example = "72.3")
        Double weight,

        @Schema(description = "골격근량", example = "34.1")
        Double skeletalMuscleMass,

        @Schema(description = "체지방량", example = "18.2")
        Double bodyFatMass,

        @Schema(description = "체지방률", example = "24.5")
        Double bodyFatPercentage,

        @Schema(description = "BMI", example = "23.1")
        Double bmi,

        @Schema(description = "기록 생성 시각")
        LocalDateTime createdAt
) {
}
