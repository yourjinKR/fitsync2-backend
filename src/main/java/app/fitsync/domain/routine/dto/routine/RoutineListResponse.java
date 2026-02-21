package app.fitsync.domain.routine.dto.routine;


import app.fitsync.domain.routine.dto.exercise.RoutineExerciseListResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Routine list item")
public record RoutineListResponse(
        long id,
        String name,
        long writerId,
        long ownerId,
        int displayOrder,
        List<RoutineExerciseListResponse> routineExercises
) {

}
