package app.fitsync.domain.profile.entity;

import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.user.entity.BirthDate;
import app.fitsync.domain.user.entity.Gender;
import app.fitsync.domain.user.entity.User;
import app.fitsync.global.BaseEntity;
import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.*;

/**
 * <h2>UserProfile 엔티티</h2>
 *
 * <p>
 * 회원(UserType = MEMBER)에 한하여 관리되는 사용자 확장 정보 엔티티로,
 * 운동 루틴 추천 및 개인화 서비스를 위한 입력 데이터를 저장한다.
 * </p>
 *
 * <p>
 * 해당 엔티티는 인증 및 공통 정보를 담당하는 {@link User} 엔티티와
 * 1:1 관계를 가지며, 트레이너 계정에는 생성되지 않는다.
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>운동 목적(다중 선택) 관리</li>
 *   <li>선호 운동 카테고리 관리</li>
 *   <li>건강 및 질병 정보 저장</li>
 *   <li>키(Height) 등 비교적 변경 빈도가 낮은 신체 기본 정보 저장</li>
 *   <li>AI 운동 루틴 추천을 위한 컨텍스트 제공</li>
 * </ul>
 *
 * <h3>설계 의도</h3>
 * <ul>
 *   <li>User 엔티티 비대화를 방지하기 위한 책임 분리</li>
 *   <li>회원/트레이너 타입 분기에 따른 데이터 관리 단순화</li>
 *   <li>변경이 잦은 체성분 데이터는 {@link InBodyRecord}로 분리하여 이력 관리</li>
 * </ul>
 *
 * <h3>비고</h3>
 * <ul>
 *   <li>운동 목적 및 선호 카테고리는 다중 선택을 지원한다.</li>
 *   <li>체성분(몸무게/골격근량/체지방 등)은 별도 엔티티(InBodyRecord)로 관리한다.</li>
 * </ul>
 */
@Entity
@Table(name = "user_profiles")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 인증/공통 정보(User)와 1:1 관계
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth")
    @Embedded
    private BirthDate birth;

    @ElementCollection
    @CollectionTable(
            name = "user_workout_goals",
            joinColumns = @JoinColumn(name = "user_profile_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "workout_goal")
    @Builder.Default
    private Set<WorkoutGoal> workoutGoals = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "user_exercise_categories",
            joinColumns = @JoinColumn(name = "user_profile_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_category")
    @Builder.Default
    private Set<ExerciseCategory> exerciseCategories = new LinkedHashSet<>();

    @Column(name = "disease")
    private String disease;

    /**
     * 키(Height)는 상대적으로 변경 빈도가 낮아 프로필에 유지
     */
    @Column(name = "height")
    private Double height;

    /**
     * 인바디 측정 이력(N)
     * - 최신 인바디는 "가장 최근 측정일(createdAt)" 기준으로 조회하여 사용
     */
    @OneToMany(mappedBy = "userProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC, id DESC")
    @Builder.Default
    private Set<InBodyRecord> inBodyRecords = new LinkedHashSet<>();

    public void addInBodyRecord(InBodyRecord record) {
        this.inBodyRecords.add(record);
        record.setUserProfile(this);
    }

    public void removeInBodyRecord(InBodyRecord record) {
        this.inBodyRecords.remove(record);
        record.setUserProfile(null);
    }

    public InBodyRecord getRecentInBodyRecord() {
        return this.inBodyRecords.stream()
                .findFirst()
                .orElse(new InBodyRecord());
    }
}
