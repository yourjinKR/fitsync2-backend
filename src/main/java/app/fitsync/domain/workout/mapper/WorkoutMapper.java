package app.fitsync.domain.workout.mapper;

import app.fitsync.domain.exercise.dto.exercise.ExerciseSummaryResponse;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.workout.dto.WorkoutDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutExerciseDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutExerciseRequest;
import app.fitsync.domain.workout.dto.WorkoutListResponse;
import app.fitsync.domain.workout.dto.WorkoutRequest;
import app.fitsync.domain.workout.dto.WorkoutSetDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutSetRequest;
import app.fitsync.domain.workout.entity.Workout;
import app.fitsync.domain.workout.entity.WorkoutExercise;
import app.fitsync.domain.workout.entity.WorkoutSet;
import java.util.List;
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

    public WorkoutDetailResponse toDetailDto(Workout workout) {

        List<WorkoutExerciseDetailResponse> workoutExerciseDetailResponses = workout.getWorkoutExercises().stream()
                .map(this::toDetailDto)
                .toList();

        return new WorkoutDetailResponse(
                workout.getId(),
                workout.getWriter().getId(),
                workout.getOwner().getId(),
                workout.getMemo(),
                workout.getCreatedAt(),
                workoutExerciseDetailResponses
        );
    }

    public WorkoutExerciseDetailResponse toDetailDto(WorkoutExercise workoutExercise) {

        Exercise exercise = workoutExercise.getExercise();
        ExerciseSummaryResponse exerciseSummaryResponse = new ExerciseSummaryResponse(
                exercise.getId(),
                exercise.getName()
        );

        List<WorkoutSetDetailResponse> workoutSetDetailResponses = workoutExercise.getWorkoutSets().stream()
                .map(this::toDetailDto)
                .toList();

        return new WorkoutExerciseDetailResponse(
                workoutExercise.getId(),
                exerciseSummaryResponse,
                workoutExercise.getMemo(),
                workoutSetDetailResponses
        );
    }


    public WorkoutSetDetailResponse toDetailDto(WorkoutSet workoutSet) {

        return new WorkoutSetDetailResponse(
                workoutSet.getId(),
                workoutSet.getMemo(),
                workoutSet.getWeightKg(),
                workoutSet.getReps(),
                workoutSet.getDistanceM(),
                workoutSet.getDurationSec(),
                workoutSet.getSpeedKmh(),
                workoutSet.getRpe(),
                workoutSet.getRestTimeSec()
        );
    }



}
