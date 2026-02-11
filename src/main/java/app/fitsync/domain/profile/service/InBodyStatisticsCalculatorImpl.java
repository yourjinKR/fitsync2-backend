package app.fitsync.domain.profile.service;

import app.fitsync.domain.profile.dto.InBodyStatisticsResponse.InBodySummary;
import app.fitsync.domain.profile.entity.InBodyRecord;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InBodyStatisticsCalculatorImpl implements InBodyStatisticsCalculator {

    @Override
    public InBodySummary calculateSummary(List<InBodyRecord> records) {
        if (records == null || records.size() < InBodySummary.CALCULABLE_MIN) {
            return InBodySummary.EMPTY;
        }

        InBodyRecord current = records.get(0);
        InBodyRecord previous = records.get(1);

        return new InBodySummary(
                getDiff(current.getWeight(), previous.getWeight()),
                getRate(current.getWeight(), previous.getWeight()),
                getDiff(current.getSkeletalMuscleMass(), previous.getSkeletalMuscleMass()),
                getRate(current.getSkeletalMuscleMass(), previous.getSkeletalMuscleMass()),
                getDiff(current.getBodyFatMass(), previous.getBodyFatMass()),
                getRate(current.getBodyFatMass(), previous.getBodyFatMass()),
                "");
    }

    // 단순 차이 계산 (소수점 둘째자리 반올림)
    private Double getDiff(Double current, Double prev) {
        if (current == null || prev == null) return 0.0;
        return Math.round((current - prev) * 100) / 100.0;
    }

    // 증감률 계산 (%)
    private Double getRate(Double current, Double prev) {
        if (current == null || prev == null || prev == 0) return 0.0;
        double rate = ((current - prev) / prev) * 100;
        return Math.round(rate * 100) / 100.0;
    }
}
