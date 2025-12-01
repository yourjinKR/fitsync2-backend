package app.fitsync.domain.exercise.dto;

import app.fitsync.domain.exercise.entity.ExerciseCategory;
import java.util.List;

public record ExerciseListResponse(
        Long id,
        String name,
        ExerciseCategory category,
        boolean hidden,
        List<BodyDetailPartListResponse> detailParts
) {
}
