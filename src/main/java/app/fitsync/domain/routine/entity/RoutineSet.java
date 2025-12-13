package app.fitsync.domain.routine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_exercise_id",nullable = false)
    private RoutineExercise routineExercise;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "weight_kg")
    private int weightKg;

    @Column(name = "reps")
    private int reps;

    @Column(name = "distance_m")
    private int distanceM;

    @Column(name = "duration_sec")
    private int durationSec;

    @Column(name = "speed_kmh")
    private int speedKmh;

    @Column(name = "rpe")
    private int rpe;

    @Column(name = "rest_time_sec")
    private int restTimeSec;
}
