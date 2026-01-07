package app.fitsync.domain.exercise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h2>BodyPart 엔티티</h2>
 *
 * <p>
 * 신체 부위의 <b>상위 그룹(대분류)</b>을 나타내는 엔티티
 * 예: "상체", "하체", "코어", "팔", "등" 등
 * </p>
 *
 * <p>
 * {@link BodyDetailPart}와 1:N 관계를 가지며,
 * 실제 타겟 지정은 주로 세부 부위(BodyDetailPart)에서 수행됩니다.
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>운동 타겟 부위의 상위 분류 제공</li>
 *   <li>UI(드롭다운/카테고리)에서 2단 선택(대분류→세부분류)을 지원</li>
 * </ul>
 *
 * <h3>설계 의도</h3>
 * <ul>
 *   <li>부위 계층 구조를 엔티티로 분리해 데이터 확장/관리 용이</li>
 *   <li>세부 부위 확장 시 상위 분류의 중복을 줄임</li>
 * </ul>
 */
@Getter
@Entity
@Table(name = "body_parts")
@NoArgsConstructor
public class BodyPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "bodyPart")
    private List<BodyDetailPart> detailParts = new ArrayList<>();

    @Override
    public String toString() {
        return "BodyPart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
