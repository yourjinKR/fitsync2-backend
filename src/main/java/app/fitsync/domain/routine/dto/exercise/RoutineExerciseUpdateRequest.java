package app.fitsync.domain.routine.dto.exercise;

import app.fitsync.domain.routine.dto.set.RoutineSetDeleteRequest;
import app.fitsync.domain.routine.dto.set.RoutineSetRequest;
import app.fitsync.domain.routine.dto.set.RoutineSetUpdateRequest;
import app.fitsync.domain.routine.entity.RoutineExercise;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RoutineExerciseUpdateRequest(

        @NotNull
        long id,

        @NotNull
        int displayOrder,

        @NotNull
        @Size(max = RoutineExercise.DESCRIPTION_MAX_LENGTH)
        String description,

        @Nullable
        @Valid
        List<RoutineSetRequest> newRoutineSets,

        @Nullable
        @Valid
        List<RoutineSetUpdateRequest> updateRoutineSets,

        @Nullable
        @Valid
        List<RoutineSetDeleteRequest> deleteRoutineSets
) {
}
