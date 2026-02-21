package app.fitsync.domain.exercise.dto.exercise;

import app.fitsync.domain.exercise.dto.target.ExerciseTargetDetailResponse;
import app.fitsync.domain.exercise.entity.EffectType;
import app.fitsync.domain.exercise.entity.Equipment;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.entity.MetricType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Schema(description = "Exercise detail response")
public record ExerciseDetailResponse(
        Long id,
        String name,
        ExerciseCategory category,
        String description,
        Map<String, Object> details,
        boolean hidden,
        List<ExerciseTargetDetailResponse> targets,
        Set<EffectType> effects,
        Set<Equipment> equipments,
        Set<MetricType> requiredMetric
) {
}
