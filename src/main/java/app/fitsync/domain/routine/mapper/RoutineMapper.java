package app.fitsync.domain.routine.mapper;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseRequest;
import app.fitsync.domain.routine.dto.routine.RoutineRequest;
import app.fitsync.domain.routine.dto.set.RoutineSetRequest;
import app.fitsync.domain.routine.entity.Routine;
import app.fitsync.domain.routine.entity.RoutineExercise;
import app.fitsync.domain.routine.entity.RoutineSet;
import app.fitsync.domain.user.entity.User;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RoutineMapper {

    public Routine toEntity(RoutineRequest request, List<RoutineExercise> routineExercises, User writer, User owner) {

        Routine routine = Routine.builder()
                .name(request.name())
                .writer(writer)
                .owner(owner)
                .displayOrder(request.displayOrder())
                .description(request.description())
                .build();

        routine.addAllExercises(routineExercises);

        return routine;
    }

    public RoutineExercise toEntity(RoutineExerciseRequest request, Exercise exercises) {

        RoutineExercise routineExercise = RoutineExercise.builder()
//                .routine()
                .exercise(exercises)
                .displayOrder(request.displayOrder())
                .description(request.description())
                .build();

        List<RoutineSet> sets = request.routineSets().stream()
                .map(this::toEntity)
                .toList();

        routineExercise.addAllSets(sets);
        return routineExercise;
    }

    public RoutineSet toEntity(RoutineSetRequest request) {

        return RoutineSet.builder()
//                .routineExercise()
                .displayOrder(request.displayOrder())
                .weightKg(request.weightKg())
                .reps(request.reps())
                .distanceM(request.distanceM())
                .durationSec(request.durationSec())
                .speedKmh(request.speedKmh())
                .rpe(request.rpe())
                .restTimeSec(request.restTimeSec())
                .build();
    }
}
