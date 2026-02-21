package app.fitsync.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 루틴 추천 결과
 */
@Schema(description = "AI routine recommendation response")
public record AIRoutineResponse(
        @Schema(description = "Recommended routines")
        List<Routine> result

) {
    @Schema(description = "Routine item")
    public record Routine(
            String name,
            List<RoutineExercise> routineExercises
    ) { }

    @Schema(description = "Routine exercise item")
    public record RoutineExercise(
            long exerciseId,
            String exerciseName,
            List<RoutineSet> routineSets
    ) { }

    @Schema(description = "Routine set item")
    public record RoutineSet(
            Integer weightKg,
            Integer reps,
            Integer distanceM,
            Integer durationSec,
            Integer speedKmh,
            Integer rpe,
            Integer restTimeSec
    ) { }
}
