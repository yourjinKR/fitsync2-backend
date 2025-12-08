package app.fitsync.domain.exercise.dto.target;

import app.fitsync.domain.exercise.entity.TargetRole;
import jakarta.validation.constraints.NotNull;

public record ExerciseTargetRequest(

        @NotNull
        Long bodyDetailPartId,

        @NotNull
        TargetRole targetRole
) {

}
