package app.fitsync.domain.workout.dto;

public record WorkoutSetDetailResponse(
        long id,
        String memo,
        Integer weightKg,
        Integer reps,
        Integer distanceM,
        Integer durationSec,
        Integer speedKmh,
        Integer rpe,
        Integer restTimeSec
) {
}
