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
        routineExercises.removeIf(re -> re.getId().equals(routineExerciseId));
    }
}
