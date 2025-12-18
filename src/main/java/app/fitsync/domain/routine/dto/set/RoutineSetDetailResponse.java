package app.fitsync.domain.routine.dto.set;


public record RoutineSetDetailResponse(
        long id,
        int displayOrder,
        Integer weightKg,
        Integer reps,
        Integer distanceM,
        Integer durationSec,
        Integer speedKmh,
        Integer rpe,
        Integer restTimeSec
) {
}
