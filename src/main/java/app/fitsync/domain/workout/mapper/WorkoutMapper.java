package app.fitsync.domain.workout.mapper;

import app.fitsync.domain.workout.dto.WorkoutExerciseRequest;
import app.fitsync.domain.workout.dto.WorkoutListResponse;
import app.fitsync.domain.workout.dto.WorkoutRequest;
import app.fitsync.domain.workout.dto.WorkoutSetRequest;
import app.fitsync.domain.workout.entity.Workout;
import app.fitsync.domain.workout.entity.WorkoutExercise;
import app.fitsync.domain.workout.entity.WorkoutSet;
import org.springframework.stereotype.Component;

@Component
public class WorkoutMapper {

    public Workout toEntity(WorkoutRequest request) {

        return Workout.builder()
//                .writer()
//                .owner()
                .memo(request.memo())
//                .workoutExercises()
                .build();
    }

    public WorkoutExercise toEntity(WorkoutExerciseRequest request) {

        return WorkoutExercise.builder()
//                .workout()
//                .exercise()
                .memo(request.memo())
//                .workoutSets()
                .build();

    }

    public WorkoutSet toEntity(WorkoutSetRequest request) {

        return WorkoutSet.builder()
//                .workoutExercise()
                .memo(request.memo())
                .weightKg(request.weightKg())
                .reps(request.reps())
                .distanceM(request.distanceM())
                .durationSec(request.durationSec())
                .speedKmh(request.speedKmh())
                .rpe(request.rpe())
                .restTimeSec(request.restTimeSec())
                .build();
    }

    public WorkoutListResponse toDto(Workout workout) {

        return new WorkoutListResponse(
                workout.getId(),
                workout.getCreatedAt()
        );
    }
}
