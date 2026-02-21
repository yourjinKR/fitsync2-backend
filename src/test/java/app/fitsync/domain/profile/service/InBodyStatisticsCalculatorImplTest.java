package app.fitsync.domain.profile.service;

import app.fitsync.domain.profile.dto.InBodyStatisticsResponse.InBodySummary;
import app.fitsync.domain.profile.entity.InBodyRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InBodyStatisticsCalculatorImplTest {

    private final InBodyStatisticsCalculatorImpl calculator = new InBodyStatisticsCalculatorImpl();

    @Test
    @DisplayName("레코드 2건 이상이면 증감/증감률을 계산한다")
    void calculateSummary_withTwoRecords_returnsCalculatedValues() {
        InBodyRecord current = InBodyRecord.builder()
                .weight(70.0)
                .skeletalMuscleMass(35.0)
                .bodyFatMass(15.0)
                .build();
        InBodyRecord previous = InBodyRecord.builder()
                .weight(68.0)
                .skeletalMuscleMass(34.0)
                .bodyFatMass(16.0)
                .build();

        InBodySummary summary = calculator.calculateSummary(List.of(current, previous));

        assertThat(summary.weightChange()).isEqualTo(2.0);
        assertThat(summary.weightChangeRate()).isEqualTo(2.94);
        assertThat(summary.muscleMassChange()).isEqualTo(1.0);
        assertThat(summary.muscleMassChangeRate()).isEqualTo(2.94);
        assertThat(summary.bodyFatMassChange()).isEqualTo(-1.0);
        assertThat(summary.bodyFatMassChangeRate()).isEqualTo(-6.25);
    }

    @Test
    @DisplayName("레코드가 2건 미만이면 EMPTY 요약을 반환한다")
    void calculateSummary_withInsufficientRecords_returnsEmptySummary() {
        InBodyRecord onlyOne = InBodyRecord.builder().weight(70.0).build();

        InBodySummary summary = calculator.calculateSummary(List.of(onlyOne));

        assertThat(summary).isEqualTo(InBodySummary.EMPTY);
    }
}

