package app.fitsync.domain.routine.dto;


import jakarta.validation.constraints.NotNull;

public record RoutineSetRequest(

        @NotNull
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
