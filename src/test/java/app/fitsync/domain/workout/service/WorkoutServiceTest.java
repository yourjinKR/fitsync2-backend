package app.fitsync.domain.workout.service;

import app.fitsync.domain.exercise.dto.exercise.ExerciseSummaryResponse;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.exception.ExerciseErrorCode;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.exception.UserException;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.domain.workout.dto.WorkoutDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutExerciseDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutExerciseRequest;
import app.fitsync.domain.workout.dto.WorkoutListRequest;
import app.fitsync.domain.workout.dto.WorkoutListResponse;
import app.fitsync.domain.workout.dto.WorkoutRequest;
import app.fitsync.domain.workout.dto.WorkoutResponse;
import app.fitsync.domain.workout.dto.WorkoutSetDetailResponse;
import app.fitsync.domain.workout.dto.WorkoutSetRequest;
import app.fitsync.domain.workout.entity.Workout;
import app.fitsync.domain.workout.entity.WorkoutExercise;
import app.fitsync.domain.workout.entity.WorkoutSet;
import app.fitsync.domain.workout.mapper.WorkoutMapper;
import app.fitsync.domain.workout.repository.WorkoutRepository;
import app.fitsync.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private WorkoutMapper workoutMapper;

    @Test
    @DisplayName("TS-WORKOUT-001: 운동 기록 생성 성공(운동/세트 포함)")
    void createWorkout_success() {
        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseRepository, workoutMapper);

        WorkoutSetRequest setRequest = new WorkoutSetRequest("set", 1, 60, 10, null, null, null, null, 90);
        WorkoutExerciseRequest exerciseRequest = new WorkoutExerciseRequest(100L, "exercise", List.of(setRequest));
        WorkoutRequest request = new WorkoutRequest(1L, 2L, "memo", List.of(exerciseRequest));

        User writer = org.mockito.Mockito.mock(User.class);
        User owner = org.mockito.Mockito.mock(User.class);
        Exercise exercise = org.mockito.Mockito.mock(Exercise.class);
        WorkoutExercise mappedExercise = WorkoutExercise.builder().memo("exercise").build();
        WorkoutSet mappedSet = WorkoutSet.builder().memo("set").weightKg(60).reps(10).restTimeSec(90).build();
        Workout saved = Workout.builder().id(99L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(writer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(owner));
        when(exerciseRepository.findById(100L)).thenReturn(Optional.of(exercise));
        when(workoutMapper.toEntity(exerciseRequest)).thenReturn(mappedExercise);
        when(workoutMapper.toEntity(setRequest)).thenReturn(mappedSet);
        when(workoutRepository.save(org.mockito.ArgumentMatchers.any(Workout.class))).thenReturn(saved);

        WorkoutResponse response = workoutService.create(request);

        assertThat(response.id()).isEqualTo(99L);

        ArgumentCaptor<Workout> captor = ArgumentCaptor.forClass(Workout.class);
        verify(workoutRepository).save(captor.capture());
        Workout captured = captor.getValue();
        assertThat(captured.getWorkoutExercises()).hasSize(1);
        WorkoutExercise capturedExercise = captured.getWorkoutExercises().get(0);
        assertThat(capturedExercise.getExercise()).isEqualTo(exercise);
        assertThat(capturedExercise.getWorkoutSets()).hasSize(1);
        assertThat(capturedExercise.getWorkoutSets().get(0).getMemo()).isEqualTo("set");
    }

    @Test
    @DisplayName("TS-WORKOUT-002: 없는 owner/writer/exercise 참조 시 실패")
    void createWorkout_failsWhenOwnerWriterOrExerciseNotFound() {
        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseRepository, workoutMapper);

        WorkoutSetRequest setRequest = new WorkoutSetRequest("set", 1, 60, 10, null, null, null, null, 90);
        WorkoutExerciseRequest exerciseRequest = new WorkoutExerciseRequest(100L, "exercise", List.of(setRequest));

        WorkoutRequest missingOwner = new WorkoutRequest(1L, 2L, "memo", List.of(exerciseRequest));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> workoutService.create(missingOwner))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(UserException.NOT_FOUND);

        WorkoutRequest missingWriter = new WorkoutRequest(1L, 2L, "memo", List.of(exerciseRequest));
        User owner = org.mockito.Mockito.mock(User.class);
        when(userRepository.findById(2L)).thenReturn(Optional.of(owner));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> workoutService.create(missingWriter))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(UserException.NOT_FOUND);

        WorkoutRequest missingExercise = new WorkoutRequest(1L, 2L, "memo", List.of(exerciseRequest));
        User writer = org.mockito.Mockito.mock(User.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(writer));
        WorkoutExercise mappedExercise = WorkoutExercise.builder().memo("exercise").build();
        WorkoutSet mappedSet = WorkoutSet.builder().memo("set").build();
        when(workoutMapper.toEntity(exerciseRequest)).thenReturn(mappedExercise);
        when(workoutMapper.toEntity(setRequest)).thenReturn(mappedSet);
        when(exerciseRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutService.create(missingExercise))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(ExerciseErrorCode.NOT_FOUND);
    }

    @Test
    @DisplayName("TS-WORKOUT-003: ownerId로 workout 목록을 조회한다")
    void viewList_filtersByOwnerId() {
        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseRepository, workoutMapper);

        Pageable pageable = PageRequest.of(0, 5);
        long ownerId = 7L;
        WorkoutListRequest request = new WorkoutListRequest(ownerId);

        Workout workout = org.mockito.Mockito.mock(Workout.class);
        when(workoutRepository.findByOwnerId(ownerId, pageable)).thenReturn(new PageImpl<>(List.of(workout)));
        when(workoutMapper.toDto(workout)).thenReturn(new WorkoutListResponse(1L, LocalDateTime.now()));

        Page<WorkoutListResponse> result = workoutService.viewList(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(workoutRepository).findByOwnerId(ownerId, pageable);
        verify(workoutRepository, never()).findAll(pageable);
        verify(workoutMapper).toDto(workout);
    }

    @Test
    @DisplayName("ownerId가 없으면 전체 workout 목록을 조회한다")
    void viewList_withoutOwnerId_usesFindAll() {
        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseRepository, workoutMapper);

        Pageable pageable = PageRequest.of(0, 5);
        WorkoutListRequest request = new WorkoutListRequest(null);

        Workout workout = org.mockito.Mockito.mock(Workout.class);
        when(workoutRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(workout)));
        when(workoutMapper.toDto(workout)).thenReturn(new WorkoutListResponse(2L, LocalDateTime.now()));

        Page<WorkoutListResponse> result = workoutService.viewList(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(workoutRepository).findAll(pageable);
        verify(workoutRepository, never()).findByOwnerId(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.any(Pageable.class));
    }

    @Test
    @DisplayName("TS-WORKOUT-004: 상세 조회에 세트/운동 요약 포함")
    void viewDetail_includesExerciseSummaryAndSets() {
        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseRepository, workoutMapper);

        long workoutId = 30L;
        Workout workout = org.mockito.Mockito.mock(Workout.class);
        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));

        WorkoutDetailResponse mapped = new WorkoutDetailResponse(
                workoutId,
                1L,
                2L,
                "memo",
                LocalDateTime.now(),
                List.of(new WorkoutExerciseDetailResponse(
                        11L,
                        new ExerciseSummaryResponse(100L, "squat"),
                        "exercise memo",
                        List.of(new WorkoutSetDetailResponse(21L, "set memo", 60, 10, null, null, null, null, 90))
                ))
        );
        when(workoutMapper.toDetailDto(workout)).thenReturn(mapped);

        WorkoutDetailResponse response = workoutService.viewDetail(workoutId);

        assertThat(response.workoutExercises()).hasSize(1);
        assertThat(response.workoutExercises().get(0).exercise().name()).isEqualTo("squat");
        assertThat(response.workoutExercises().get(0).workoutSets()).hasSize(1);
        assertThat(response.workoutExercises().get(0).workoutSets().get(0).reps()).isEqualTo(10);
        verify(workoutMapper).toDetailDto(workout);
    }
}

