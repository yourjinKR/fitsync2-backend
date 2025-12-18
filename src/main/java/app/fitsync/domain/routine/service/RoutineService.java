package app.fitsync.domain.routine.service;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseRequest;
import app.fitsync.domain.routine.dto.routine.RoutineDetailResponse;
import app.fitsync.domain.routine.dto.routine.RoutineListResponse;
import app.fitsync.domain.routine.dto.routine.RoutineRequest;
import app.fitsync.domain.routine.dto.routine.RoutineResponse;
import app.fitsync.domain.routine.entity.Routine;
import app.fitsync.domain.routine.entity.RoutineExercise;
import app.fitsync.domain.routine.exception.RoutineErrorCode;
import app.fitsync.domain.routine.mapper.RoutineMapper;
import app.fitsync.domain.routine.repository.RoutineRepository;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public RoutineResponse createRoutine(RoutineRequest request) {

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

    @Override
    @Transactional
    public Page<RoutineListResponse> getRoutineList(Pageable pageable, Long ownerId, Long writerId) {
        return routineRepository.search(pageable, ownerId, writerId).map(routineMapper::toDto);
    }

    @Override
    @Transactional
    public RoutineDetailResponse findRoutine(long routineId) {

        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RestApiException(RoutineErrorCode.NOT_FOUND, routineId));

        return routineMapper.toDetailDto(routine);
    }
}
