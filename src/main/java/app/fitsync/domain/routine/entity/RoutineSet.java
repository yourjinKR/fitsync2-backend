package app.fitsync.domain.routine.entity;

import app.fitsync.domain.routine.dto.set.RoutineSetUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>RoutineSet 엔티티</h2>
 *
 * <p>
 * 루틴 운동({@link RoutineExercise})의 “세트 1개 단위”를 나타내는 엔티티
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>세트 순서 및 목표 수행값(무게/횟수/거리/시간 등) 저장</li>
 *   <li>유산소/웨이트 등 다양한 운동 타입을 한 세트 모델로 수용</li>
 * </ul>
 *
 * <h3>설계 의도</h3>
 * <ul>
 *   <li>MetricType과 1:1로 강제하지 않고 컬럼을 두어 범용 세트 모델로 구성</li>
 *   <li>필요한 값만 채우는 형태로 사용</li>
 * </ul>
 *
 * <h3>비고</h3>
 * <ul>
 *   <li>routineExercise는 PROTECTED setter로 외부에서 임의 변경을 제한</li>
 * </ul>
 */
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routine_sets")
public class RoutineSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_exercise_id",nullable = false)
    private RoutineExercise routineExercise;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "weight_kg")
    private Integer weightKg;

    @Column(name = "reps")
    private Integer reps;

    @Column(name = "distance_m")
    private Integer distanceM;

    @Column(name = "duration_sec")
    private Integer durationSec;

    @Column(name = "speed_kmh")
    private Integer speedKmh;

    @Column(name = "rpe")
    private Integer rpe;

    @Column(name = "rest_time_sec")
    private Integer restTimeSec;

    public void updateFrom(RoutineSetUpdateRequest request) {
        this.displayOrder = request.displayOrder();
        this.weightKg = request.weightKg();
        this.reps = request.reps();
        this.distanceM = request.distanceM();
        this.durationSec = request.durationSec();
        this.speedKmh = request.speedKmh();
        this.rpe = request.rpe();
        this.restTimeSec = request.restTimeSec();
    }
}
