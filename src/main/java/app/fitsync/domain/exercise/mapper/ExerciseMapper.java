package app.fitsync.domain.exercise.mapper;

import app.fitsync.domain.exercise.dto.ExerciseRequest;
import app.fitsync.domain.exercise.dto.ExerciseTargetRequest;
import app.fitsync.domain.exercise.entity.BodyDetailPart;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseTarget;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public List<ExerciseTarget> toEntities(List<BodyDetailPart> detailParts, List<ExerciseTargetRequest> targetRequests) {
        Map<Long, BodyDetailPart> partMap = detailParts.stream()
                .collect(Collectors.toMap(BodyDetailPart::getId, part -> part));

        return targetRequests.stream()
                .map(request -> {
                    BodyDetailPart part = partMap.get(request.bodyDetailPartId());

                    if (part == null) {
                        throw new IllegalArgumentException("존재하지 않는 운동 부위 ID입니다: " + request.bodyDetailPartId());
                    }

                    return new ExerciseTarget(part, request.targetRole());
                })
                .toList();
    }
}
