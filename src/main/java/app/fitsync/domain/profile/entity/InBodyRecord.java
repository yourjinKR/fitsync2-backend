package app.fitsync.domain.profile.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * <h2>InBodyRecord 엔티티</h2>
 *
 * <p>
 * 사용자의 체성분(인바디) 측정 결과를 저장하는 이력 엔티티이다.
 * 몸무게, 골격근량, 체지방량, 체지방률, BMI 등 변경이 빈번한 수치 데이터를
 * 누적 관리하여 시계열 분석 및 개인화 추천에 활용한다.
 * </p>
 *
 * <p>
 * {@link UserProfile}과 N:1 관계를 가지며,
 * 한 사용자 프로필은 여러 건의 인바디 측정 기록을 가질 수 있다.
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>인바디 측정 수치(체중, 골격근량, 체지방 등) 저장</li>
 *   <li>측정 시점(createdAt) 기준 이력(History) 관리</li>
 *   <li>최신 측정값 및 변화 추이(증감/추세) 계산 기반 제공</li>
 * </ul>
 *
 * <h3>설계 의도</h3>
 * <ul>
 *   <li>변경이 잦은 수치를 UserProfile에서 분리하여 변경/이력 관리 최적화</li>
 *   <li>향후 그래프/통계/AI 피드백 등 확장 기능을 위한 데이터 축적</li>
 *   <li>측정일 기반 정렬 및 최신값 조회를 쉽게 하기 위한 구조</li>
 * </ul>
 *
 * <h3>비고</h3>
 * <ul>
 *   <li>수치 단위(kg, %, BMI 등)는 서비스 정책에 따라 프론트/백엔드에서 일관되게 유지해야 한다.</li>
 * </ul>
 */
@Entity
@Table(name = "inbody_records",
        indexes = {
                @Index(name = "idx_inbody_user_profile_created_at", columnList = "user_profile_id, created_at")
        })
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InBodyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 소속 사용자 프로필(N:1)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "skeletal_muscle_mass")
    private Double skeletalMuscleMass;

    @Column(name = "body_fat_mass")
    private Double bodyFatMass;

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;

    @Column(name = "bmi")
    private Double bmi;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 연관관계 편의 메서드를 위해 접근 범위를 제한적으로 열어둠
     */
    void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
