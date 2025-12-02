package app.fitsync.domain.exercise.dto.exercise;

import app.fitsync.domain.exercise.dto.target.ExerciseTargetUpdateRequest;
import app.fitsync.domain.exercise.entity.EffectType;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ExerciseUpdateRequest(
        String name,
        ExerciseCategory category,
        String description,
        Map<String, Object> details,
        boolean hidden,
        List<ExerciseTargetUpdateRequest> targets,
        Set<EffectType> effects
) {
}
