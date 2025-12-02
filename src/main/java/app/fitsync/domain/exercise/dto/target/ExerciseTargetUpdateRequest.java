package app.fitsync.domain.exercise.dto.target;

import app.fitsync.domain.exercise.dto.body.BodyDetailPartUpdateRequest;
import app.fitsync.domain.exercise.entity.TargetRole;

public record ExerciseTargetUpdateRequest(
        Long id,
        BodyDetailPartUpdateRequest bodyDetailPart,
        TargetRole targetRole
) {
}
