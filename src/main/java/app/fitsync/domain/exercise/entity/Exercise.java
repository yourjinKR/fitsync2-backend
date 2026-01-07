package app.fitsync.domain.exercise.entity;

import app.fitsync.domain.exercise.dto.exercise.ExerciseUpdateRequest;
import app.fitsync.global.BaseEntity;
import app.fitsync.global.config.JsonMapConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h2>Exercise 엔티티</h2>
 *
 * <p>
 * 서비스 내 “운동(Exercise)”의 마스터 데이터를 표현하는 핵심 엔티티
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>운동명/설명/카테고리 등 기본 정보 제공</li>
 *   <li>타겟 부위({@link ExerciseTarget}) 및 효과({@link EffectType}) 등 메타 정보 관리</li>
 *   <li>추천/검색/루틴 생성 등 여러 도메인의 기준 데이터</li>
 * </ul>
 *
 * <h3>설계 의도</h3>
 * <ul>
 *   <li>운동에 대한 확장 속성(난이도, 팁, 주의사항, 영상 링크 등)은
 *       <code>details</code> JSON 컬럼(Map)으로 유연하게 관리</li>
 *   <li>운영 중 스키마 변경 부담을 줄이고, 프론트/AI 컨텍스트 확장에 대응</li>
 * </ul>
 *
 * <h3>비고</h3>
 * <ul>
 *   <li><code>hidden</code>은 “삭제 대신 숨김(soft hide)”을 위한 플래그로 활용 가능</li>
 *   <li>toString에서 연관 컬렉션 출력은 Lazy 로딩/성능에 영향을 줄 수 있어 운영 시 주의</li>
 * </ul>
 */
@Getter
@Entity
@Table(name = "exercises")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise extends BaseEntity {

    public static final int NAME_MAX_LENGTH = 100;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name")
    private String name;


    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ExerciseCategory category;


    @Column(name = "description")
    private String description;


    @Convert(converter = JsonMapConverter.class)
    @Column(name = "details", columnDefinition = "json")
    @Builder.Default
    private Map<String, Object> details = new HashMap<>();


    @Column(name = "hidden")
    @Builder.Default
    private boolean hidden = false;


    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExerciseTarget> targets = new ArrayList<>();


    @ElementCollection(targetClass = EffectType.class)
    @CollectionTable(
            name = "exercise_effects",
            joinColumns = @JoinColumn(name = "exercise_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "effect_type")
    @Builder.Default
    private Set<EffectType> effects = new HashSet<>();


    @ElementCollection(targetClass = Equipment.class)
    @CollectionTable(
            name = "exercise_equipments",
            joinColumns = @JoinColumn(name = "exercise_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_name")
    @Builder.Default
    private Set<Equipment> equipments = new HashSet<>();


    @ElementCollection(targetClass = MetricType.class)
    @CollectionTable(
            name = "exercise_metrics",
            joinColumns = @JoinColumn(name = "exercise_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type")
    @Builder.Default
    private Set<MetricType> requiredMetrics = new HashSet<>();


    public void addTarget(ExerciseTarget target) {
        this.targets.add(target);
        target.setExercise(this);
    }


    public void addAllTargets(List<ExerciseTarget> targets) {
        for (ExerciseTarget target : targets) {
            addTarget(target);
        }
    }


    public void updateFrom(ExerciseUpdateRequest request) {
        this.name = request.name();
        this.category = request.category();
        this.description = request.description();
        this.details = request.details();
        this.hidden = request.hidden();
        this.effects = request.effects();
        this.requiredMetrics = request.requiredMetrics();
    }


    public void hide() {
        this.hidden = true;
        setDeletedAt(LocalDateTime.now());
    }


    public void show() {
        this.hidden = false;
    }


    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", details=" + details +
                ", hidden=" + hidden +
                ", targets=" + targets +
                ", effects=" + effects +
                '}';
    }
}
