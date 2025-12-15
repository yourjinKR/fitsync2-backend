package app.fitsync.domain.routine.dto.routine;

import app.fitsync.domain.routine.dto.exercise.RoutineExerciseRequest;
import app.fitsync.domain.routine.entity.Routine;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RoutineRequest(

        @NotNull
        @Size(max = Routine.NAME_MAX_LENGTH)
        String name,

        @NotNull
        long writerId,

        @NotNull
        long ownerId,

        @NotNull
        int displayOrder,

        @NotNull
        @Size(max = Routine.DESCRIPTION_MAX_LENGTH)
        String description,

        @NotNull
        @Valid
        List<RoutineExerciseRequest> routineExercises
) {

}
