package app.fitsync.domain.exercise.dto.exercise;

import app.fitsync.domain.exercise.dto.target.ExerciseTargetRequest;
import app.fitsync.domain.exercise.entity.EffectType;
import app.fitsync.domain.exercise.entity.Equipment;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.entity.MetricType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ExerciseRequest(

        @NotNull
        @Size(max = Exercise.NAME_MAX_LENGTH)
        String name,

        @NotNull
        ExerciseCategory category,

        @NotNull
        @Size(max = Exercise.DESCRIPTION_MAX_LENGTH)
        String description,

        @NotNull
        Map<String, Object> details,

        @NotNull
        @Valid
        List<ExerciseTargetRequest> targets,

        @NotNull
        @Valid
        Set<EffectType> effects,

        @NotNull
        @Valid
        Set<Equipment> equipments,

        @NotNull
        @Valid
        Set<MetricType> requiredMetrics
) {
}
