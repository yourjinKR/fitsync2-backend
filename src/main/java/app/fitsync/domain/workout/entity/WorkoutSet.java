package app.fitsync.domain.workout.entity;

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

@Entity
@Table(name = "workout_sets")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Column(name = "memo")
    private String memo;

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
}
