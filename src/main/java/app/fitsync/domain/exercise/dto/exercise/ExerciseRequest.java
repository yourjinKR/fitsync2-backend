package app.fitsync.domain.exercise.dto.exercise;

import app.fitsync.domain.exercise.dto.target.ExerciseTargetRequest;
import app.fitsync.domain.exercise.entity.EffectType;
import app.fitsync.domain.exercise.entity.Equipment;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.entity.MetricType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Schema(description = "운동 생성 요청")
public record ExerciseRequest(

        @NotNull
        @Size(max = Exercise.NAME_MAX_LENGTH)
        @Schema(description = "운동명", example = "Barbell Squat")
        String name,

        @NotNull
        @Schema(description = "운동 카테고리", example = "FITNESS")
        ExerciseCategory category,

        @NotNull
        @Size(max = Exercise.DESCRIPTION_MAX_LENGTH)
        @Schema(description = "운동 설명", example = "하체 전반 근력 강화 운동")
        String description,

        @NotNull
        @Schema(description = "확장 상세 정보(JSON)")
        Map<String, Object> details,

        @NotNull
        @Valid
        @Schema(description = "타겟 부위 목록")
        List<ExerciseTargetRequest> targets,

        @NotNull
        @Valid
        @Schema(description = "운동 효과 목록")
        Set<EffectType> effects,

        @NotNull
        @Valid
        @Schema(description = "필요 장비 목록")
        Set<Equipment> equipments,

        @NotNull
        @Valid
        @Schema(description = "필수 측정 지표 목록")
        Set<MetricType> requiredMetrics
) {
}
