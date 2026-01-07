package app.fitsync.domain.exercise.entity;

import app.fitsync.domain.exercise.dto.target.ExerciseTargetUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * <h2>ExerciseTarget 엔티티</h2>
 *
 * <p>
 * 특정 운동({@link Exercise})이 자극하는 <b>신체 세부 부위({@link BodyDetailPart})</b>를 연결하는 매핑 엔티티
 * </p>
 *
 * <h3>왜 매핑 엔티티인가?</h3>
 * <ul>
 *   <li>Exercise - BodyDetailPart 는 다대다 형태가 자연스러움</li>
 *   <li>“주/보조 타겟 역할({@link TargetRole})” 같은 추가 속성이 존재함</li>
 *   <li>단순 @ManyToMany 대신 <b>중간 엔티티(ExerciseTarget)</b>로 모델링</li>
 * </ul>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>운동 타겟 부위(세부) 지정</li>
 *   <li>타겟 역할(MAIN/SUB)로 가중치/우선순위 표현</li>
 *   <li>운동 수정 시 타겟 정보 업데이트 단위 제공</li>
 * </ul>
 */
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercise_targets")
public class ExerciseTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_detail_part_id", nullable = false)
    private BodyDetailPart bodyDetailPart;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_role", nullable = false)
    private TargetRole targetRole;

    public ExerciseTarget(BodyDetailPart bodyDetailPart, TargetRole targetRole) {
        this.bodyDetailPart = bodyDetailPart;
        this.targetRole = targetRole;
    }

    public void updateFrom(ExerciseTargetUpdateRequest request) {
        this.targetRole = request.targetRole();
    }

    public void clearExercise() {
        this.exercise = null;
    }

    @Override
    public String toString() {
        return "ExerciseTarget{" +
                "id=" + id +
                ", bodyDetailPart=" + bodyDetailPart +
                ", targetRole=" + targetRole +
                '}';
    }
}
