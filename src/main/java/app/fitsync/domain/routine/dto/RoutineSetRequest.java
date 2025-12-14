package app.fitsync.domain.routine.dto;


public record RoutineSetRequest(
        int displayOrder,
        int weightKg,
        int reps,
        int distanceM,
        int durationSec,
        int speedKmh,
        int rpe,
        int restTimeSec
) {
}
