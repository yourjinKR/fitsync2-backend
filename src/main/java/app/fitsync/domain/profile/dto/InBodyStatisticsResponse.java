package app.fitsync.domain.profile.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <h1>인바디 통계 응답 DTO</h1>
 * <ul>
 *     <li>전월 대비 증감률 (Summary)</li>
 *     <li>그래프용 시계열 데이터 (Trends))</li>
 * </ul>
 */
public record InBodyStatisticsResponse(
        InBodySummary summary,
        List<InBodyTrendElement> trends
) {
    /**
     * 전월 대비 증감 수치 요약
     */
    public record InBodySummary(
            Double weightChange,
            Double weightChangeRate,

            Double muscleMassChange,
            Double muscleMassChangeRate,

            Double bodyFatMassChange,
            Double bodyFatMassChangeRate
    ) {
        public static int CALCULABLE_MIN = 2;
        public static InBodySummary EMPTY = new InBodySummary(
                0D,
                0D,
                0D,
                0D,
                0D,
                0D);
    }

    /**
     * 차트 렌더링을 위한 개별 기록 요소
     */
    public record InBodyTrendElement(
            LocalDateTime date,
            Double weight,
            Double skeletalMuscleMass,
            Double bodyFatMass,
            Double bodyFatPercentage,
            Double bmi
    ) {}
}