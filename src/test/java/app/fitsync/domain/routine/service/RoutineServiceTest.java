package app.fitsync.domain.routine.service;

import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.routine.dto.routine.RoutineListResponse;
import app.fitsync.domain.routine.entity.Routine;
import app.fitsync.domain.routine.mapper.RoutineMapper;
import app.fitsync.domain.routine.repository.RoutineRepository;
import app.fitsync.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutineServiceTest {

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoutineMapper routineMapper;

    @Test
    @DisplayName("TS-ROUTINE-004: routine list는 ownerId/writerId 필터를 지정한 의미 그대로 전달한다")
    void getRoutineList_passesOwnerAndWriterFiltersToRepository() {
        RoutineService routineService = new RoutineService(
                routineRepository,
                exerciseRepository,
                userRepository,
                routineMapper
        );

        Pageable pageable = PageRequest.of(0, 5);
        Long ownerId = 10L;
        Long writerId = 20L;

        Routine routine = org.mockito.Mockito.mock(Routine.class);
        when(routineRepository.search(pageable, ownerId, writerId))
                .thenReturn(new PageImpl<>(List.of(routine)));
        when(routineMapper.toDto(routine))
                .thenReturn(new RoutineListResponse(1L, "routine", writerId, ownerId, 1, List.of()));

        Page<RoutineListResponse> result = routineService.getRoutineList(pageable, ownerId, writerId);

        assertThat(result.getContent()).hasSize(1);
        verify(routineRepository).search(pageable, ownerId, writerId);
        verify(routineMapper).toDto(routine);
    }
}

