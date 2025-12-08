package app.fitsync.domain.exercise.dto.target;

import app.fitsync.domain.exercise.entity.TargetRole;
import jakarta.validation.constraints.NotNull;

public record ExerciseTargetUpdateRequest(

        @NotNull
        Long id,

        @NotNull
        TargetRole targetRole
) {
}
