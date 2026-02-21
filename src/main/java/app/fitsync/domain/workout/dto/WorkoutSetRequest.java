package app.fitsync.domain.workout.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record WorkoutSetRequest(
        @Size(max = 1000)
        String memo,

        @NotNull
        @Positive
        Integer displayOrder,

        @Positive
        Integer weightKg,

        @Positive
        Integer reps,

        @Positive
        Integer distanceM,

        @Positive
        Integer durationSec,

        @Positive
        Integer speedKmh,

        @Positive
        Integer rpe,

        @Positive
        Integer restTimeSec
) {
}
