package app.fitsync.domain.exercise.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;


import app.fitsync.domain.exercise.dto.ExerciseRequest;
import app.fitsync.domain.exercise.dto.ExerciseResponse;
import app.fitsync.domain.exercise.dto.ExerciseTargetRequest;
import app.fitsync.domain.exercise.entity.BodyDetailPart;
import app.fitsync.domain.exercise.entity.EffectType;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.entity.TargetRole;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.BodyDetailPartRepository;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @InjectMocks
    ExerciseService exerciseService;

    @Mock
    ExerciseRepository exerciseRepository;

    @Mock
    BodyDetailPartRepository bodyDetailPartRepository;

    @Mock
    ExerciseMapper exerciseMapper;

    ExerciseRequest request;

    @BeforeEach
    void setup() {
        request = new ExerciseRequest(
                "벤치 프레스",
                ExerciseCategory.FITNESS,
                "가슴을 쓰는 운동임",
                Map.of("detail", List.of("하나", "둘", "셋")),
                List.of(
                        new ExerciseTargetRequest(1L, TargetRole.MAIN),
                        new ExerciseTargetRequest(2L, TargetRole.SUB)
                ),
                Set.of(EffectType.STRENGTH)
        );
    }

    @Test
    @DisplayName("데이터_POST_성공")
    public void successCreateExercise() {
        // given - setUp() 함수로

        // Repository subbing
        given(bodyDetailPartRepository.getReferenceById(anyLong()))
                .willReturn(new BodyDetailPart());

        // Mapper Stubbing & ID 주입 준비
        Exercise exerciseToSave = new Exercise();

        // 레포지토리가 리턴할 저장 후 Exercise 객체
        Exercise savedExercise = new Exercise();

        // 리플렉션을 사용하여 필드에 강제 주입
        ReflectionTestUtils.setField(savedExercise, "id", 100L);

        // 매퍼가 호출되면 toSave를 반환
        given(exerciseMapper.toEntity(any(ExerciseRequest.class), anyList()))
                .willReturn(exerciseToSave);

        // 엔티티 저장시 ID가 있는 exercise 엔티티를 리턴
        given(exerciseRepository.save(any(Exercise.class)))
                .willReturn(savedExercise);

        // when (실행)
        ExerciseResponse response = exerciseService.createExercise(request);

        // 아까 주입한 id값인지?
        assertThat(response.id()).isEqualTo(100L);

        // 레포지토리가 2번 호출했는지? - 객체가 2개이기에
        verify(bodyDetailPartRepository, times(2)).getReferenceById(anyLong());

        // save는 한번 호출됐는지?
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }
}