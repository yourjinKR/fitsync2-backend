package app.fitsync.domain.exercise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h2>BodyDetailPart 엔티티</h2>
 *
 * <p>
 * 신체 부위의 <b>세부 단위(중/소분류)</b>를 나타내는 엔티티
 * 예: 상체(BodyPart) - 가슴/등/어깨(BodyDetailPart)
 * </p>
 *
 * <p>
 * 운동의 타겟 정보({@link ExerciseTarget})는 보통 이 엔티티를 참조하여<br>
 * “어떤 운동이 어떤 세부 부위를 자극하는지”를 표현한다.
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>운동 타겟 부위 지정의 기준 데이터</li>
 *   <li>추천/분석에서 부위별 볼륨/빈도 계산의 키</li>
 * </ul>
 *
 * <h3>연관관계 주의</h3>
 * <ul>
 *   <li>ManyToOne은 기본적으로 LAZY를 권장 (N+1 방지)</li>
 *   <li>toString에서 연관 엔티티 출력 시 지연로딩/순환참조에 주의</li>
 * </ul>
 */
@Getter
@Entity
@Table(name = "body_detail_parts")
@NoArgsConstructor
public class BodyDetailPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_part_id", nullable = false)
    private BodyPart bodyPart;

    @Override
    public String toString() {
        return "BodyDetailPart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
