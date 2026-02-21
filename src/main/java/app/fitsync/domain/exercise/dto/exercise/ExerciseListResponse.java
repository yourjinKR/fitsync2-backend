package app.fitsync.domain.exercise.dto.exercise;

import app.fitsync.domain.exercise.dto.body.BodyDetailPartListResponse;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Exercise list item")
public record ExerciseListResponse(
        Long id,
        String name,
        ExerciseCategory category,
        boolean hidden,
        List<BodyDetailPartListResponse> detailParts
) {
}
