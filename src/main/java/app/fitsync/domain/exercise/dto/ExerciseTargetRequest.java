package app.fitsync.domain.exercise.dto;

import app.fitsync.domain.exercise.entity.TargetRole;

public record ExerciseTargetRequest(
        Long bodyDetailPartId,
        TargetRole targetRole
) {

}
