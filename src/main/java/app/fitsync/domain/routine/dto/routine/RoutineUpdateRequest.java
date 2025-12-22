package app.fitsync.domain.routine.dto.routine;

import app.fitsync.domain.routine.dto.exercise.RoutineExerciseDeleteRequest;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseRequest;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseUpdateRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RoutineUpdateRequest(

        @NotNull
        String name,

        @NotNull
        int displayOrder,

        @NotNull
        String description,

        @Nullable
        @Valid
        List<RoutineExerciseRequest> newExercises,

        @Nullable
        @Valid
        List<RoutineExerciseUpdateRequest> updateExercises,

        @Nullable
        @Valid
        List<RoutineExerciseDeleteRequest> deleteExercises
) {
}
