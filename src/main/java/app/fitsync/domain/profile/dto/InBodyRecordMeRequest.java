package app.fitsync.domain.profile.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InBodyRecordMeRequest(
        @NotNull
        @Positive
        Double weight,

        @NotNull
        @Positive
        Double skeletalMuscleMass,

        @NotNull
        @Positive
        Double bodyFatMass,

        @NotNull
        @Positive
        Double bodyFatPercentage,

        @NotNull
        @Positive
        Double bmi
) {
}
