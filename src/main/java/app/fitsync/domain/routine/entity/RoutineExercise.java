package app.fitsync.domain.routine.entity;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.routine.dto.exercise.RoutineExerciseUpdateRequest;
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
                .orElseThrow(IllegalArgumentException::new);
    }

    public void deleteRoutineSet(long routineSetId) {
        routineSets.removeIf(routineSet -> routineSet.getId().equals(routineSetId));
    }
}
