package app.fitsync.domain.routine.service;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.routine.dto.RoutineExerciseRequest;
import app.fitsync.domain.routine.dto.RoutineRequest;
import app.fitsync.domain.routine.dto.RoutineResponse;
import app.fitsync.domain.routine.entity.Routine;
import app.fitsync.domain.routine.entity.RoutineExercise;
import app.fitsync.domain.routine.mapper.RoutineMapper;
import app.fitsync.domain.routine.repository.RoutineRepository;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineService implements RoutineServiceInterface{
    public final RoutineRepository routineRepository;
    public final ExerciseRepository exerciseRepository;
    public final UserRepository userRepository;
    public final RoutineMapper routineMapper;

    @Override
    @Transactional
    public RoutineResponse create(RoutineRequest request) {
        User writer = userRepository.getReferenceById(request.writerId());
        User owner = userRepository.getReferenceById(request.ownerId());

        List<RoutineExercise> routineExercises = request.routineExercises().stream()
                .map(this::createRoutineExercise)
                .toList();

        Routine routine = routineMapper.toEntity(request, routineExercises, writer, owner);
        Routine savedRoutine = routineRepository.save(routine);

        return new RoutineResponse(savedRoutine.getId());
    }


    public RoutineExercise createRoutineExercise(RoutineExerciseRequest request) {
        long exerciseId = request.exerciseId();
        Exercise exercise = exerciseRepository.getReferenceById(exerciseId);

        return routineMapper.toEntity(request, exercise);
    }

}
