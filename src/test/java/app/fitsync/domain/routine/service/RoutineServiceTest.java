package app.fitsync.domain.routine.service;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseDeleteRequest;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseRequest;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseUpdateRequest;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
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
    @DisplayName("TS-ROUTINE-001: 루틴 생성 성공(운동/세트 포함)")
    void createRoutine_success() {
        RoutineService routineService = new RoutineService(routineRepository, exerciseRepository, userRepository, routineMapper);

        RoutineSetRequest setRequest = new RoutineSetRequest(1, 60, 10, null, null, null, null, 90);
        RoutineExerciseRequest exerciseRequest = new RoutineExerciseRequest(100L, 1, "desc", List.of(setRequest));
        RoutineRequest request = new RoutineRequest("routine", 1L, 2L, 1, "memo", List.of(exerciseRequest));

        User writer = org.mockito.Mockito.mock(User.class);
        User owner = org.mockito.Mockito.mock(User.class);
        Exercise exercise = org.mockito.Mockito.mock(Exercise.class);
        RoutineExercise routineExercise = org.mockito.Mockito.mock(RoutineExercise.class);
        Routine mapped = org.mockito.Mockito.mock(Routine.class);
        Routine saved = Routine.builder().id(99L).build();

        when(userRepository.getReferenceById(1L)).thenReturn(writer);
        when(userRepository.getReferenceById(2L)).thenReturn(owner);
        when(exerciseRepository.getReferenceById(100L)).thenReturn(exercise);
        when(routineMapper.toEntity(exerciseRequest, exercise)).thenReturn(routineExercise);
        when(routineMapper.toEntity(request, List.of(routineExercise), writer, owner)).thenReturn(mapped);
        when(routineRepository.save(mapped)).thenReturn(saved);

        RoutineResponse response = routineService.createRoutine(request);

        assertThat(response.id()).isEqualTo(99L);
        verify(routineRepository).save(mapped);
    }

    @Test
    @DisplayName("TS-ROUTINE-002: 루틴 수정 시 new/update/delete 동시 처리")
    void updateRoutine_handlesNewUpdateDeleteTogether() {
        RoutineService routineService = new RoutineService(routineRepository, exerciseRepository, userRepository, routineMapper);

        long routineId = 10L;
        long deleteExerciseId = 101L;
        long updateExerciseId = 102L;
        long newExerciseId = 103L;
        long deleteSetId = 201L;
        long updateSetId = 202L;

        RoutineSetRequest newSet = new RoutineSetRequest(1, 40, 12, null, null, null, null, 60);
        RoutineSetUpdateRequest updateSet = new RoutineSetUpdateRequest(updateSetId, 2, 50, 10, null, null, null, null, 90);
        RoutineSetDeleteRequest deleteSet = new RoutineSetDeleteRequest(deleteSetId);

        RoutineExerciseRequest newExercise = new RoutineExerciseRequest(newExerciseId, 3, "new", List.of(newSet));
        RoutineExerciseUpdateRequest updateExercise = new RoutineExerciseUpdateRequest(
                updateExerciseId,
                2,
                "update",
                List.of(newSet),
                List.of(updateSet),
                List.of(deleteSet)
        );
        RoutineExerciseDeleteRequest deleteExercise = new RoutineExerciseDeleteRequest(deleteExerciseId);

        RoutineUpdateRequest request = new RoutineUpdateRequest(
                "updated",
                2,
                "updated desc",
                List.of(newExercise),
                List.of(updateExercise),
                List.of(deleteExercise)
        );

        Routine routine = org.mockito.Mockito.mock(Routine.class);
        RoutineExercise routineExercise = org.mockito.Mockito.mock(RoutineExercise.class);
        RoutineSet routineSet = org.mockito.Mockito.mock(RoutineSet.class);
        RoutineSet mappedNewSet = org.mockito.Mockito.mock(RoutineSet.class);
        Exercise exercise = org.mockito.Mockito.mock(Exercise.class);
        RoutineExercise mappedNewExercise = org.mockito.Mockito.mock(RoutineExercise.class);

        when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
        when(routine.findRoutineExercise(updateExerciseId)).thenReturn(routineExercise);
        when(routineExercise.findRoutineSet(updateSetId)).thenReturn(routineSet);
        when(exerciseRepository.getReferenceById(newExerciseId)).thenReturn(exercise);
        when(routineMapper.toEntity(newExercise, exercise)).thenReturn(mappedNewExercise);
        when(routineMapper.toEntity(newSet)).thenReturn(mappedNewSet);

        RoutineResponse response = routineService.updateRoutine(routineId, request);

        assertThat(response.id()).isEqualTo(routineId);
        verify(routine).deleteRoutineExercise(deleteExerciseId);
        verify(routineExercise).updateFrom(updateExercise);
        verify(routineExercise).deleteRoutineSet(deleteSetId);
        verify(routineSet).updateFrom(updateSet);
        verify(routineExercise).addAllSets(anyList());
        verify(routine).addAllExercises(List.of(mappedNewExercise));
    }

    @Test
    @DisplayName("TS-ROUTINE-003: 없는 루틴 ID 수정 시 NOT_FOUND")
    void updateRoutine_notFoundRoutine_throwsNotFound() {
        RoutineService routineService = new RoutineService(routineRepository, exerciseRepository, userRepository, routineMapper);
        when(routineRepository.findById(999L)).thenReturn(Optional.empty());

        RoutineUpdateRequest request = new RoutineUpdateRequest("n", 1, "d", null, null, null);

        assertThatThrownBy(() -> routineService.updateRoutine(999L, request))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(RoutineErrorCode.NOT_FOUND);
    }

    @Test
    @DisplayName("TS-ROUTINE-003: 없는 운동 ID 수정 시 EXERCISE_NOT_FOUND")
    void updateRoutine_notFoundExercise_throwsExerciseNotFound() {
        RoutineService routineService = new RoutineService(routineRepository, exerciseRepository, userRepository, routineMapper);

        long routineId = 1L;
        long missingExerciseId = 123L;
        Routine routine = org.mockito.Mockito.mock(Routine.class);
        when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
        when(routine.findRoutineExercise(missingExerciseId))
                .thenThrow(new RestApiException(RoutineErrorCode.EXERCISE_NOT_FOUND, missingExerciseId));

        RoutineExerciseUpdateRequest updateExercise = new RoutineExerciseUpdateRequest(
                missingExerciseId, 1, "d", null, null, null
        );
        RoutineUpdateRequest request = new RoutineUpdateRequest("n", 1, "d", null, List.of(updateExercise), null);

        assertThatThrownBy(() -> routineService.updateRoutine(routineId, request))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(RoutineErrorCode.EXERCISE_NOT_FOUND);
    }

    @Test
    @DisplayName("TS-ROUTINE-003: 없는 세트 ID 수정 시 SET_NOT_FOUND")
    void updateRoutine_notFoundSet_throwsSetNotFound() {
        RoutineService routineService = new RoutineService(routineRepository, exerciseRepository, userRepository, routineMapper);

        long routineId = 1L;
        long exerciseId = 10L;
        long missingSetId = 200L;

        Routine routine = org.mockito.Mockito.mock(Routine.class);
        RoutineExercise routineExercise = org.mockito.Mockito.mock(RoutineExercise.class);
        when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
        when(routine.findRoutineExercise(exerciseId)).thenReturn(routineExercise);
        when(routineExercise.findRoutineSet(missingSetId))
                .thenThrow(new RestApiException(RoutineErrorCode.SET_NOT_FOUND, missingSetId));

        RoutineSetUpdateRequest updateSet = new RoutineSetUpdateRequest(missingSetId, 1, null, null, null, null, null, null, null);
        RoutineExerciseUpdateRequest updateExercise = new RoutineExerciseUpdateRequest(
                exerciseId, 1, "d", null, List.of(updateSet), null
        );
        RoutineUpdateRequest request = new RoutineUpdateRequest("n", 1, "d", null, List.of(updateExercise), null);

        assertThatThrownBy(() -> routineService.updateRoutine(routineId, request))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(RoutineErrorCode.SET_NOT_FOUND);
    }

    @Test
    @DisplayName("TS-ROUTINE-004: routine list는 ownerId/writerId 필터를 지정한 의미 그대로 전달한다")
    void getRoutineList_passesOwnerAndWriterFiltersToRepository() {
        RoutineService routineService = new RoutineService(routineRepository, exerciseRepository, userRepository, routineMapper);

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
