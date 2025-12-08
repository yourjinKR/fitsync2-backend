package app.fitsync.domain.exercise.dto.target;

import jakarta.validation.constraints.NotNull;

public record ExerciseTargetDeleteRequest(

        @NotNull
        Long id
) {
}
