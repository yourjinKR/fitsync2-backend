package app.fitsync.domain.exercise.dto.target;

import app.fitsync.domain.exercise.dto.body.BodyDetailPartResponse;
import app.fitsync.domain.exercise.entity.TargetRole;

public record ExerciseTargetDetailResponse(
        Long id,
        BodyDetailPartResponse bodyDetailPart,
        TargetRole targetRole
) {

}
