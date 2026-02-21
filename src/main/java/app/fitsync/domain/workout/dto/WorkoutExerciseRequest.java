package app.fitsync.domain.workout.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record WorkoutExerciseRequest(
        @Positive
        long exerciseId,

        @Size(max = 1000)
        String memo,

        @NotNull
        @NotEmpty
        @Valid
        List<WorkoutSetRequest> sets
) {
}
