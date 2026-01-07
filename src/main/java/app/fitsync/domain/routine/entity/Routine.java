package app.fitsync.domain.routine.entity;

import app.fitsync.domain.routine.dto.routine.RoutineUpdateRequest;
import app.fitsync.domain.routine.exception.RoutineErrorCode;
import app.fitsync.domain.user.entity.User;
import app.fitsync.global.BaseEntity;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h2>Routine 엔티티</h2>
 *
 * <p>
 * 사용자 루틴(운동 계획)의 상위 단위를 나타내는 엔티티
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>루틴 기본 정보(이름/메모/정렬 순서) 관리</li>
 *   <li>루틴에 포함된 운동 목록({@link RoutineExercise}) 관리</li>
 *   <li>루틴 작성자/소유자({@link User}) 구분</li>
 * </ul>
 *
 * <h3>설계 의도</h3>
 * <ul>
 *   <li>Routine → RoutineExercise → RoutineSet 계층으로 루틴 구조를 분리</li>
 *   <li>cascade + orphanRemoval로 하위 요소 생명주기를 루틴에 종속</li>
 * </ul>
 *
 * <h3>비고</h3>
 * <ul>
 *   <li>writer/owner를 분리해 공유/복사/템플릿 확장 여지</li>
 *   <li>find/delete는 없을 때 예외를 명확히 던지는 형태</li>
 * </ul>
 */
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routines")
public class Routine extends BaseEntity {

    public static final int NAME_MAX_LENGTH = 100;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "displayName")
    private int displayOrder;

    @Column(name = "memo")
    private String description;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RoutineExercise> routineExercises = new ArrayList<>();

    public void addExercise(RoutineExercise routineExercise) {
        this.routineExercises.add(routineExercise);
        routineExercise.setRoutine(this);
    }

    public void addAllExercises(List<RoutineExercise> routineExercises) {
        routineExercises.forEach(this::addExercise);
    }

    public void updateFrom(RoutineUpdateRequest request) {
        this.name = request.name();
        this.displayOrder = request.displayOrder();
        this.description = request.description();
    }

    public RoutineExercise findRoutineExercise(long routineExerciseId) {
        return routineExercises.stream()
                .filter(routineExercise -> routineExercise.getId() == routineExerciseId)
                .findFirst()
                .orElseThrow(() -> new RestApiException(RoutineErrorCode.EXERCISE_NOT_FOUND, routineExerciseId));
    }

    public void deleteRoutineExercise(long routineExerciseId) {
        boolean removeIf = routineExercises.removeIf(re -> re.getId().equals(routineExerciseId));
        if (!removeIf) {
            throw new RestApiException(RoutineErrorCode.EXERCISE_NOT_FOUND, routineExerciseId);
        }
    }
}
