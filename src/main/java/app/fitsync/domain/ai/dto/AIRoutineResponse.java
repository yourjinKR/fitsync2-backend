package app.fitsync.domain.ai.dto;

import java.util.List;

/**
 * 루틴 추천 결과
 */
public record AIRoutineResponse(
        String name,
        List<RoutineExercise> routineExercises

) {
    public record RoutineExercise(
            long exerciseId,
            String exerciseName,
            List<RoutineSet> routineSets
    ) { }

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
