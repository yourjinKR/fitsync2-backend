package app.fitsync.domain.exercise.dto.target;

import app.fitsync.domain.exercise.entity.TargetRole;

public record ExerciseTargetUpdateRequest(
        Long id,
        TargetRole targetRole
) {
}
