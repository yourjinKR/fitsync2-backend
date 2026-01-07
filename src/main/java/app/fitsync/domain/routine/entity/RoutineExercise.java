package app.fitsync.domain.routine.entity;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseUpdateRequest;
import app.fitsync.domain.routine.exception.RoutineErrorCode;
import app.fitsync.global.exception.RestApiException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>RoutineExercise 엔티티</h2>
 *
 * <p>
 * 루틴에 포함된 “운동 1개 단위”를 나타내는 엔티티
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>루틴({@link Routine})과 운동 마스터({@link Exercise}) 연결</li>
 *   <li>루틴 내 표시 순서/메모 관리</li>
 *   <li>세트 목록({@link RoutineSet}) 관리</li>
 * </ul>
 *
 * <h3>설계 의도</h3>
 * <ul>
 *   <li>RoutineExercise → RoutineSet으로 세부 수행 계획을 분리</li>
 *   <li>cascade + orphanRemoval로 세트 생명주기 종속</li>
 * </ul>
 */
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routine_exercises")
public class RoutineExercise {

    public static final int DESCRIPTION_MAX_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id",nullable = false)
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "displayName")
    private int displayOrder;

    @Column(name = "memo")
    private String description;

    @OneToMany(mappedBy = "routineExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RoutineSet> routineSets = new ArrayList<>();

    public void addSet(RoutineSet routineSet) {
        this.routineSets.add(routineSet);
        routineSet.setRoutineExercise(this);
    }

    public void addAllSets(List<RoutineSet> routineSets) {
        routineSets.forEach(this::addSet);
    }

    public void updateFrom(RoutineExerciseUpdateRequest request) {
        this.displayOrder = request.displayOrder();
        this.description = request.description();
    }

    public RoutineSet findRoutineSet(long routineSetId) {
        return routineSets.stream()
                .filter(routineSet -> routineSet.getId() == routineSetId)
                .findFirst()
                .orElseThrow(() -> new RestApiException(RoutineErrorCode.SET_NOT_FOUND, routineSetId));
    }

    public void deleteRoutineSet(long routineSetId) {
        boolean removeIf = routineSets.removeIf(routineSet -> routineSet.getId().equals(routineSetId));
        if (!removeIf) {
            throw new RestApiException(RoutineErrorCode.SET_NOT_FOUND, routineSetId);
        }
    }
}
