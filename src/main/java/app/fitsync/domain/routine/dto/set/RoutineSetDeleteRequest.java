package app.fitsync.domain.routine.dto.set;

import jakarta.validation.constraints.NotNull;

public record RoutineSetDeleteRequest(
        @NotNull
        long id
) {
}
