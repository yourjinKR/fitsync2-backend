package app.fitsync.domain.profile.dto;

public record InBodyRecordRequest(
        long userId,
        Double weight,
        Double skeletalMuscleMass,
        Double bodyFatMass,
        Double bodyFatPercentage,
        Double bmi
) {
}
