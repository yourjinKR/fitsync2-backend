package app.fitsync.domain.routine.service;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseDeleteRequest;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseRequest;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseUpdateRequest;
import app.fitsync.domain.routine.dto.routine.RoutineDetailResponse;
import app.fitsync.domain.routine.dto.routine.RoutineListResponse;
import app.fitsync.domain.routine.dto.routine.RoutineRequest;
import app.fitsync.domain.routine.dto.routine.RoutineResponse;
import app.fitsync.domain.routine.dto.routine.RoutineUpdateRequest;
import app.fitsync.domain.routine.dto.set.RoutineSetDeleteRequest;
import app.fitsync.domain.routine.dto.set.RoutineSetRequest;
import app.fitsync.domain.routine.dto.set.RoutineSetUpdateRequest;
import app.fitsync.domain.routine.entity.Routine;
import app.fitsync.domain.routine.entity.RoutineExercise;
import app.fitsync.domain.routine.entity.RoutineSet;
import app.fitsync.domain.routine.exception.RoutineErrorCode;
import app.fitsync.domain.routine.mapper.RoutineMapper;
import app.fitsync.domain.routine.repository.RoutineRepository;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@NullMarked
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

        Routine routine = findRoutineById(routineId);
        return routineMapper.toDetailDto(routine);
    }

    public Routine findRoutineById(long routineId) {
        return routineRepository.findById(routineId)
                .orElseThrow(() -> new RestApiException(RoutineErrorCode.NOT_FOUND, routineId));
    }


    @Override
    @Transactional
    public RoutineResponse updateRoutine(long routineId, RoutineUpdateRequest request) {

        Routine routine = findRoutineById(routineId);
        routine.updateFrom(request);

        List<RoutineExerciseRequest> routineExerciseRequests = request.newExercises();
        List<RoutineExerciseUpdateRequest> routineExerciseUpdateRequests = request.updateExercises();
        List<RoutineExerciseDeleteRequest> routineExerciseDeleteRequests = request.deleteExercises();

        if (routineExerciseRequests != null) {

            List<RoutineExercise> newRoutineExercises = routineExerciseRequests.stream()
                    .map(this::createRoutineExercise)
                    .toList();

            routine.addAllExercises(newRoutineExercises);
        }

        if (routineExerciseUpdateRequests != null) {

            for (RoutineExerciseUpdateRequest routineExerciseUpdateRequest : routineExerciseUpdateRequests) {

                RoutineExercise routineExercise = routine.findRoutineExercise(routineExerciseUpdateRequest.id());
                updateRoutineExercise(routineExercise, routineExerciseUpdateRequest);
            }
        }

        if (routineExerciseDeleteRequests != null) {

            routineExerciseDeleteRequests.forEach(deleteRequest -> {
                long id = deleteRequest.id();
                routine.deleteRoutineExercise(id);
            });
        }

        return new RoutineResponse(routineId);
    }


    public void updateRoutineExercise(RoutineExercise routineExercise, RoutineExerciseUpdateRequest request) {
        routineExercise.updateFrom(request);

        List<RoutineSetRequest> newRoutineSets = request.newRoutineSets();
        List<RoutineSetUpdateRequest> updateRoutineSets = request.updateRoutineSets();
        List<RoutineSetDeleteRequest> deleteRoutineSets = request.deleteRoutineSets();

        if (newRoutineSets != null) {

            List<RoutineSet> routineSets = request.newRoutineSets().stream()
                    .map(routineMapper::toEntity)
                    .toList();

            routineExercise.addAllSets(routineSets);
        }

        if (updateRoutineSets != null) {

            for (RoutineSetUpdateRequest updateRoutineSet : updateRoutineSets) {

                long routineSetId = updateRoutineSet.id();
                RoutineSet routineSet = routineExercise.findRoutineSet(routineSetId);
                routineSet.updateFrom(updateRoutineSet);
            }
        }

        if (deleteRoutineSets != null) {

            for (RoutineSetDeleteRequest deleteRoutineSet : deleteRoutineSets) {

                long id = deleteRoutineSet.id();
                routineExercise.deleteRoutineSet(id);
            }
        }
    }
}
