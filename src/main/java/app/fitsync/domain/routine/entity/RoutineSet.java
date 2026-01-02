package app.fitsync.domain.routine.entity;

import app.fitsync.domain.routine.dto.set.RoutineSetUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routine_sets")
public class RoutineSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_exercise_id",nullable = false)
    private RoutineExercise routineExercise;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "weight_kg")
    private Integer weightKg;

    @Column(name = "reps")
    private Integer reps;

    @Column(name = "distance_m")
    private Integer distanceM;

    @Column(name = "duration_sec")
    private Integer durationSec;

    @Column(name = "speed_kmh")
    private Integer speedKmh;

    @Column(name = "rpe")
    private Integer rpe;

    @Column(name = "rest_time_sec")
    private Integer restTimeSec;

    public void updateFrom(RoutineSetUpdateRequest request) {
        this.displayOrder = request.displayOrder();
        this.weightKg = request.weightKg();
        this.reps = request.reps();
        this.distanceM = request.distanceM();
        this.durationSec = request.durationSec();
        this.speedKmh = request.speedKmh();
        this.rpe = request.rpe();
        this.restTimeSec = request.restTimeSec();
    }
}
