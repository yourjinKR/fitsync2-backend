package app.fitsync.domain.exercise.dto.target;

import app.fitsync.domain.exercise.entity.TargetRole;

public record ExerciseTargetRequest(
        Long bodyDetailPartId,
        TargetRole targetRole
) {

}
