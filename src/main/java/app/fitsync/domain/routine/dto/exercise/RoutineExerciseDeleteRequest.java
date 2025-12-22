package app.fitsync.domain.routine.dto.exercise;

import jakarta.validation.constraints.NotNull;

public record RoutineExerciseDeleteRequest(

        @NotNull
        long id
) {
}
