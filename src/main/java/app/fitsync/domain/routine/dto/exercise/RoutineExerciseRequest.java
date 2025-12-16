package app.fitsync.domain.routine.dto.exercise;

import app.fitsync.domain.routine.dto.set.RoutineSetRequest;
import app.fitsync.domain.routine.entity.RoutineExercise;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RoutineExerciseRequest(

        @NotNull
        long exerciseId,

        @NotNull
        int displayOrder,

        @NotNull
        @Size(max = RoutineExercise.DESCRIPTION_MAX_LENGTH)
        String description,

        @NotNull
        @Valid
        List<RoutineSetRequest> routineSets
) {
}
