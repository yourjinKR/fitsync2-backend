package app.fitsync.domain.exercise.dto.exercise;

import app.fitsync.domain.exercise.dto.target.ExerciseTargetRequest;
import app.fitsync.domain.exercise.entity.EffectType;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ExerciseRequest(
        String name,
        ExerciseCategory category,
        String description,
        Map<String, Object> details,
        List<ExerciseTargetRequest> targets,
        Set<EffectType> effects
) {
}
