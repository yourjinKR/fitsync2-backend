package app.fitsync.domain.workout.service;

import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.domain.workout.dto.WorkoutListRequest;
import app.fitsync.domain.workout.dto.WorkoutListResponse;
import app.fitsync.domain.workout.entity.Workout;
import app.fitsync.domain.workout.mapper.WorkoutMapper;
import app.fitsync.domain.workout.repository.WorkoutRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("TS-WORKOUT-003: ownerId로 workout 목록을 조회한다")
    void viewList_filtersByOwnerId() {
        WorkoutService workoutService = new WorkoutService(
                workoutRepository,
                userRepository,
                exerciseRepository,
                workoutMapper
        );

        Pageable pageable = PageRequest.of(0, 5);
        long ownerId = 7L;
        WorkoutListRequest request = new WorkoutListRequest(ownerId);

        Workout workout = org.mockito.Mockito.mock(Workout.class);
        when(workoutRepository.findByOwnerId(ownerId, pageable))
                .thenReturn(new PageImpl<>(List.of(workout)));
        when(workoutMapper.toDto(workout))
                .thenReturn(new WorkoutListResponse(1L, LocalDateTime.now()));

        Page<WorkoutListResponse> result = workoutService.viewList(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(workoutRepository).findByOwnerId(ownerId, pageable);
        verify(workoutRepository, never()).findAll(pageable);
        verify(workoutMapper).toDto(workout);
    }

    @Test
    @DisplayName("ownerId가 없으면 전체 workout 목록을 조회한다")
    void viewList_withoutOwnerId_usesFindAll() {
        WorkoutService workoutService = new WorkoutService(
                workoutRepository,
                userRepository,
                exerciseRepository,
                workoutMapper
        );

        Pageable pageable = PageRequest.of(0, 5);
        WorkoutListRequest request = new WorkoutListRequest(null);

        Workout workout = org.mockito.Mockito.mock(Workout.class);
        when(workoutRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(workout)));
        when(workoutMapper.toDto(workout))
                .thenReturn(new WorkoutListResponse(2L, LocalDateTime.now()));

        Page<WorkoutListResponse> result = workoutService.viewList(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(workoutRepository).findAll(pageable);
        verify(workoutRepository, never()).findByOwnerId(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.any(Pageable.class));
    }
}

