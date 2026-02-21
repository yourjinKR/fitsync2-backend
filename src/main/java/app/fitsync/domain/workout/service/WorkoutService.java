package app.fitsync.domain.workout.service;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.exception.ExerciseErrorCode;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.exception.UserException;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.domain.workout.dto.WorkoutDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutExerciseRequest;
import app.fitsync.domain.workout.dto.WorkoutListRequest;
import app.fitsync.domain.workout.dto.WorkoutListResponse;
import app.fitsync.domain.workout.dto.WorkoutRequest;
import app.fitsync.domain.workout.dto.WorkoutResponse;
import app.fitsync.domain.workout.entity.Workout;
import app.fitsync.domain.workout.entity.WorkoutExercise;
import app.fitsync.domain.workout.entity.WorkoutSet;
import app.fitsync.domain.workout.mapper.WorkoutMapper;
import app.fitsync.domain.workout.repository.WorkoutRepository;
import app.fitsync.global.exception.RestApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkoutService implements WorkoutServiceInterface {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutMapper workoutMapper;

    @Override
    @Transactional
    public WorkoutResponse create(WorkoutRequest request) {

        long ownerId = request.ownerId();
        long writerId = request.writerId();

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RestApiException(UserException.NOT_FOUND, ownerId));

        User writer = userRepository.findById(writerId)
                .orElseThrow(() -> new RestApiException(UserException.NOT_FOUND, writerId));

        List<WorkoutExercise> workoutExercises = request.workoutExercises().stream()
                .map(this::createWorkoutExercise)
                .toList();

        Workout workout = Workout.builder()
                .owner(owner)
                .writer(writer)
                .memo(request.memo())
                .build();

        workoutExercises.forEach(workout::addWorkExercise);

        Workout save = workoutRepository.save(workout);
        return new WorkoutResponse(save.getId());
    }


    private WorkoutExercise createWorkoutExercise(WorkoutExerciseRequest request) {

        WorkoutExercise workoutExercise = workoutMapper.toEntity(request);

        List<WorkoutSet> workoutSets = request.sets().stream()
                .map(workoutMapper::toEntity)
                .toList();

        workoutSets.forEach(workoutExercise::addWorkoutSet);

        long exerciseId = request.exerciseId();
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RestApiException(ExerciseErrorCode.NOT_FOUND, exerciseId));

        workoutExercise.assignExercise(exercise);

        return workoutExercise;
    }


    @Override
    public Page<WorkoutListResponse> viewList(WorkoutListRequest request, Pageable pageable) {
        Long ownerId = request.ownerId();
        if (ownerId == null) {
            return workoutRepository.findAll(pageable).map(workoutMapper::toDto);
        }
        return workoutRepository.findByOwnerId(ownerId, pageable).map(workoutMapper::toDto);
    }


    @Override
    public WorkoutDetailResponse viewDetail(long id) {

        Workout workout = workoutRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return workoutMapper.toDetailDto(workout);
    }
}
