package app.fitsync.domain.exercise.mapper;

import app.fitsync.domain.exercise.dto.BodyDetailPartListResponse;
import app.fitsync.domain.exercise.dto.BodyDetailPartResponse;
import app.fitsync.domain.exercise.dto.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.ExerciseListResponse;
import app.fitsync.domain.exercise.dto.ExerciseRequest;
import app.fitsync.domain.exercise.dto.ExerciseTargetDetailResponse;
import app.fitsync.domain.exercise.dto.ExerciseTargetRequest;
import app.fitsync.domain.exercise.entity.BodyDetailPart;
import app.fitsync.domain.exercise.entity.BodyPart;
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

    /**
     * ExerciseTarget DTO를 등록하기 위해 서비스에서 엔티티를 검색 후 <br>
     * 외부에서 함께 주입하여 매핑
     * @param detailParts 세부 부위 엔티티
     * @param targetRequests 운동 타겟 정보 생성 요청 DTO
     * @return 운동 타겟 정보 엔티티
     */
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

    /**
     * 운동 정보 엔티티를 상세 조회 DTO로 매핑 <br>
     * 내부 데이터 또한 toDto 메서드를 통해 매핑
     * @param exercise 운동 정보 엔티티
     * @return 운동 정보 상세 조회 DTO
     */
    public ExerciseDetailResponse toDto(Exercise exercise) {

        List<ExerciseTarget> targets = exercise.getTargets();
        List<ExerciseTargetDetailResponse> exerciseTargetDetailResponses = targets.stream()
                .map(this::toDto)
                .toList();

        return new ExerciseDetailResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getCategory(),
                exercise.getDescription(),
                exercise.getDetails(),
                exercise.isHidden(),
                exerciseTargetDetailResponses,
                exercise.getEffects()
        );
    }

    /**
     * 운동 타겟 엔티티를 DTO로 변환
     * @param target 운동 타겟 엔티티
     * @return 운동 타겟 DTO
     */
    public ExerciseTargetDetailResponse toDto(ExerciseTarget target) {

        BodyDetailPart detailPart = target.getBodyDetailPart();
        BodyDetailPartResponse bodyDetailPartResponse = this.toDto(detailPart);

        return new ExerciseTargetDetailResponse(
                target.getId(),
                bodyDetailPartResponse,
                target.getTargetRole()
        );
    }

    /**
     * 운동 세부 부위 엔티티를 DTO로 변환
     * @param detailPart 운동 세부 부위 엔티티
     * @return 운동 세부 부위 DTO
     */
    public BodyDetailPartResponse toDto(BodyDetailPart detailPart) {

        BodyPart part = detailPart.getBodyPart();

        return new BodyDetailPartResponse(
                detailPart.getId(),
                detailPart.getName(),
                part.getName()
        );
    }

    /**
     * 운동 정보 엔티티를 운동 정보 리스트 조회 DTO로 변환
     * @param exercise 운동 정보 엔티티
     * @return 운동 정보 리스트 DTO
     */
    public ExerciseListResponse toListDto(Exercise exercise) {

        List<ExerciseTarget> targets = exercise.getTargets();
        List<BodyDetailPartListResponse> bodyDetailPartListResponses = targets.stream()
                .map(this::toListDto)
                .toList();

        return new ExerciseListResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getCategory(),
                exercise.isHidden(),
                bodyDetailPartListResponses
        );
    }

    /**
     * 운동 타겟 정보 엔티티에서 신체부위명만 매핑
     * @param target 운동 타겟 정보 엔티티
     * @return 부위명, 세부부위명만 매핑하여 전달
     */
    public BodyDetailPartListResponse toListDto(ExerciseTarget target) {

        BodyDetailPart detailPart = target.getBodyDetailPart();
        BodyPart part = detailPart.getBodyPart();

        return new BodyDetailPartListResponse(
                detailPart.getId(),
                detailPart.getName(),
                part.getName()
        );
    }
}
