package app.fitsync.domain.exercise.dto;

import app.fitsync.domain.exercise.entity.TargetRole;

public record ExerciseTargetDetailResponse(
        Long id,
        BodyDetailPartResponse bodyDetailPart,
        TargetRole targetRole
) {

}
