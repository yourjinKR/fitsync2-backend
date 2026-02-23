package app.fitsync.domain.exercise.service;

import app.fitsync.domain.exercise.dto.exercise.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseRequest;
import app.fitsync.domain.exercise.dto.exercise.ExerciseResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseUpdateRequest;
import app.fitsync.domain.exercise.dto.body.BodyDetailPartListResponse;
import app.fitsync.domain.exercise.dto.target.ExerciseTargetDeleteRequest;
import app.fitsync.domain.exercise.dto.target.ExerciseTargetDetailResponse;
import app.fitsync.domain.exercise.dto.target.ExerciseTargetRequest;
import app.fitsync.domain.exercise.dto.target.ExerciseTargetUpdateRequest;
import app.fitsync.domain.exercise.entity.BodyDetailPart;
import app.fitsync.domain.exercise.entity.EffectType;
import app.fitsync.domain.exercise.entity.Equipment;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.entity.ExerciseTarget;
import app.fitsync.domain.exercise.entity.MetricType;
import app.fitsync.domain.exercise.entity.TargetRole;
import app.fitsync.domain.exercise.exception.ExerciseErrorCode;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.BodyDetailPartRepository;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.exercise.repository.ExerciseTargetRepository;
import app.fitsync.global.DeleteType;
import app.fitsync.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseMapper exerciseMapper;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private BodyDetailPartRepository bodyDetailPartRepository;

    @Mock
    private ExerciseTargetRepository exerciseTargetRepository;

    private ExerciseService service() {
        return new ExerciseService(exerciseMapper, exerciseRepository, bodyDetailPartRepository, exerciseTargetRepository);
    }

    private ExerciseRequest createRequest() {
        return new ExerciseRequest(
                "squat",
                ExerciseCategory.FITNESS,
                "desc",
                Map.of("difficulty", "mid"),
                List.of(new ExerciseTargetRequest(1L, TargetRole.MAIN)),
                Set.of(EffectType.STRENGTH),
                Set.of(Equipment.BARBELL),
                Set.of(MetricType.WEIGHT, MetricType.REPS)
        );
    }

    private ExerciseUpdateRequest updateRequest() {
        return new ExerciseUpdateRequest(
                "updated",
                ExerciseCategory.FITNESS,
                "updated desc",
                Map.of("difficulty", "high"),
                false,
                List.of(new ExerciseTargetRequest(2L, TargetRole.SUB)),
                List.of(new ExerciseTargetUpdateRequest(11L, TargetRole.MAIN)),
                List.of(new ExerciseTargetDeleteRequest(12L)),
                Set.of(EffectType.STRENGTH),
                Set.of(MetricType.WEIGHT)
        );
    }

    @Test
    @DisplayName("TS-EX-001: 운동 생성 성공")
    void createExercise_success() {
        ExerciseService exerciseService = service();

        ExerciseRequest request = createRequest();
        BodyDetailPart bodyDetailPart = org.mockito.Mockito.mock(BodyDetailPart.class);
        ExerciseTarget target = org.mockito.Mockito.mock(ExerciseTarget.class);
        Exercise mapped = org.mockito.Mockito.mock(Exercise.class);
        Exercise saved = Exercise.builder().id(100L).build();

        when(bodyDetailPartRepository.getReferenceById(1L)).thenReturn(bodyDetailPart);
        when(exerciseMapper.toEntities(List.of(bodyDetailPart), request.targets())).thenReturn(List.of(target));
        when(exerciseMapper.toEntity(request, List.of(target))).thenReturn(mapped);
        when(exerciseRepository.save(mapped)).thenReturn(saved);

        ExerciseResponse response = exerciseService.createExercise(request);

        assertThat(response.id()).isEqualTo(100L);
        verify(exerciseRepository).save(mapped);
    }

    @Test
    @DisplayName("TS-EX-002: 운동 상세 조회 성공")
    void findExercise_success() {
        ExerciseService exerciseService = service();

        Exercise exercise = org.mockito.Mockito.mock(Exercise.class);
        ExerciseDetailResponse dto = new ExerciseDetailResponse(
                1L,
                "squat",
                ExerciseCategory.FITNESS,
                "desc",
                Map.of(),
                false,
                List.of(new ExerciseTargetDetailResponse(11L, null, TargetRole.MAIN)),
                Set.of(),
                Set.of(),
                Set.of()
        );

        when(exerciseRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(exercise));
        when(exerciseMapper.toDto(exercise)).thenReturn(dto);

        ExerciseDetailResponse response = exerciseService.findExercise(1L);
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("TS-EX-003: 없는 운동 ID 조회 시 NOT_FOUND")
    void findExercise_notFound_throwsNotFound() {
        ExerciseService exerciseService = service();
        when(exerciseRepository.findByIdWithDetails(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> exerciseService.findExercise(999L))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(ExerciseErrorCode.NOT_FOUND);
    }

    @Test
    @DisplayName("TS-EX-004: 운동 수정 시 타겟 new/update/delete 일괄 반영")
    void updateExercise_handlesNewUpdateDeleteTogether() {
        ExerciseService exerciseService = service();

        Exercise exercise = org.mockito.Mockito.mock(Exercise.class);
        ExerciseTarget updatedTarget = org.mockito.Mockito.mock(ExerciseTarget.class);
        ExerciseTarget deletedTarget = org.mockito.Mockito.mock(ExerciseTarget.class);
        BodyDetailPart newBodyDetail = org.mockito.Mockito.mock(BodyDetailPart.class);

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(bodyDetailPartRepository.getReferenceById(2L)).thenReturn(newBodyDetail);
        when(exerciseTargetRepository.findById(11L)).thenReturn(Optional.of(updatedTarget));
        when(exerciseTargetRepository.getReferenceById(12L)).thenReturn(deletedTarget);

        ExerciseResponse response = exerciseService.updateExercise(1L, updateRequest());

        assertThat(response.id()).isNotNull();
        verify(exercise).addAllTargets(org.mockito.ArgumentMatchers.anyList());
        verify(updatedTarget).updateFrom(new ExerciseTargetUpdateRequest(11L, TargetRole.MAIN));
        verify(exerciseTargetRepository).delete(deletedTarget);
        verify(exercise).updateFrom(updateRequest());
    }

    @Test
    @DisplayName("TS-EX-006: 운동 세부 부위 목록 조회 성공")
    void getBodyDetailPartList_success() {
        ExerciseService exerciseService = service();

        BodyDetailPart part1 = org.mockito.Mockito.mock(BodyDetailPart.class);
        BodyDetailPart part2 = org.mockito.Mockito.mock(BodyDetailPart.class);
        BodyDetailPartListResponse dto1 = new BodyDetailPartListResponse(1L, "가슴", "상체");
        BodyDetailPartListResponse dto2 = new BodyDetailPartListResponse(2L, "등", "상체");

        when(bodyDetailPartRepository.findAllByOrderByIdAsc()).thenReturn(List.of(part1, part2));
        when(exerciseMapper.toListDto(part1)).thenReturn(dto1);
        when(exerciseMapper.toListDto(part2)).thenReturn(dto2);

        List<BodyDetailPartListResponse> response = exerciseService.getBodyDetailPartList();

        assertThat(response).containsExactly(dto1, dto2);
    }

    @Test
    @DisplayName("TS-EX-005: SOFT 삭제 시 hidden=true, HARD 삭제 시 실제 삭제")
    void deleteExercise_softAndHardDelete() {
        ExerciseService exerciseService = service();

        Exercise soft = Exercise.builder()
                .id(10L)
                .name("soft")
                .category(ExerciseCategory.FITNESS)
                .description("d")
                .build();
        when(exerciseRepository.findById(10L)).thenReturn(Optional.of(soft));

        ExerciseResponse softResponse = exerciseService.deleteExercise(10L, DeleteType.SOFT);
        assertThat(softResponse.id()).isEqualTo(10L);
        assertThat(soft.isHidden()).isTrue();
        verify(exerciseRepository).save(soft);

        Exercise hard = Exercise.builder()
                .id(11L)
                .name("hard")
                .category(ExerciseCategory.FITNESS)
                .description("d")
                .build();
        when(exerciseRepository.findById(11L)).thenReturn(Optional.of(hard));

        ExerciseResponse hardResponse = exerciseService.deleteExercise(11L, DeleteType.HARD);
        assertThat(hardResponse.id()).isEqualTo(11L);
        verify(exerciseRepository).delete(hard);
    }
}

