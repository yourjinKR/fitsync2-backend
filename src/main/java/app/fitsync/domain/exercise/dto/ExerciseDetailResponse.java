package app.fitsync.domain.exercise.dto;

import app.fitsync.domain.exercise.entity.EffectType;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ExerciseDetailResponse(
        Long id,
        String name,
        ExerciseCategory category,
        String description,
        Map<String, Object> details,
        boolean hidden,
        List<ExerciseTargetDetailResponse> targets,
        Set<EffectType> effects
) {
}
