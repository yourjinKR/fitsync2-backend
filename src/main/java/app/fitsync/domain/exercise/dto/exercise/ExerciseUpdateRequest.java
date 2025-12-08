package app.fitsync.domain.exercise.dto.exercise;

import app.fitsync.domain.exercise.dto.target.ExerciseTargetDeleteRequest;
import app.fitsync.domain.exercise.dto.target.ExerciseTargetRequest;
import app.fitsync.domain.exercise.dto.target.ExerciseTargetUpdateRequest;
import app.fitsync.domain.exercise.entity.EffectType;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ExerciseUpdateRequest(

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
        boolean hidden,

        @NotNull
        @Valid
        List<ExerciseTargetRequest> newTargets,

        @NotNull
        @Valid
        List<ExerciseTargetUpdateRequest> updateTargets,

        @NotNull
        @Valid
        List<ExerciseTargetDeleteRequest> deleteTargets,

        @NotNull
        Set<EffectType> effects
) {
}
