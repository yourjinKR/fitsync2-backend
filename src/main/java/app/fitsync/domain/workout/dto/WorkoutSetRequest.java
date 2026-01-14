package app.fitsync.domain.workout.dto;

public record WorkoutSetRequest(
        String memo,
        Integer displayOrder,
        Integer weightKg,
        Integer reps,
        Integer distanceM,
        Integer durationSec,
        Integer speedKmh,
        Integer rpe,
        Integer restTimeSec
) {
}
