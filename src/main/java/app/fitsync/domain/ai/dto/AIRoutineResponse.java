package app.fitsync.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 루틴 추천 결과
 */
@Schema(description = "AI 루틴 추천 응답")
public record AIRoutineResponse(
        @Schema(description = "추천 루틴 목록")
        List<Routine> result

) {
    @Schema(description = "루틴 항목")
    public record Routine(
            String name,
            List<RoutineExercise> routineExercises
    ) { }

    @Schema(description = "루틴 운동 항목")
    public record RoutineExercise(
            long exerciseId,
            String exerciseName,
            List<RoutineSet> routineSets
    ) { }

    @Schema(description = "루틴 세트 항목")
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
