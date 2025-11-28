package app.fitsync.domain.exercise.mapper;

import app.fitsync.domain.exercise.dto.ExerciseRequest;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseTarget;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExerciseMapper {
    /**
     * 운동정보 생성 요청 DTO를 Entity로 매핑하는 함수
     * @param request 운동정보 생성 요청 DTO
     * @param targets 운동-부위 연결 엔티티 (부위를 참조하기에 외부에서 주입하여 매핑)
     * @return Exercise
     */
    public Exercise toEntity(ExerciseRequest request, List<ExerciseTarget> targets) {
        Exercise exercise = Exercise.builder()
                .name(request.name())
                .category(request.category())
                .description(request.description())
                .details(request.details())
                .effects(request.effects())
                .build();

        exercise.addAllTargets(targets);

        return exercise;
    }
}
