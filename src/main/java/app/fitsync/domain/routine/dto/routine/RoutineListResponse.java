package app.fitsync.domain.routine.dto.routine;


import app.fitsync.domain.routine.dto.exercise.RoutineExerciseListResponse;
import java.util.List;

public record RoutineListResponse(
        long id,
        String name,
        long writerId,
        long ownerId,
        int displayOrder,
        List<RoutineExerciseListResponse> routineExercises
) {

}
