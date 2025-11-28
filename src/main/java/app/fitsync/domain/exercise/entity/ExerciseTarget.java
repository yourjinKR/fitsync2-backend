package app.fitsync.domain.exercise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exericse_targets")
@NoArgsConstructor
public class ExerciseTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_detail_part_id", nullable = false)
    private BodyDetailPart bodyDetailPart;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_role", nullable = false)
    private TargetRole targetRole;

    public ExerciseTarget(BodyDetailPart bodyDetailPart, TargetRole targetRole) {
        this.bodyDetailPart = bodyDetailPart;
        this.targetRole = targetRole;
    }

    @Override
    public String toString() {
        return "ExerciseTarget{" +
                "id=" + id +
                ", exercise=" + exercise +
                ", bodyDetailPart=" + bodyDetailPart +
                ", targetRole=" + targetRole +
                '}';
    }
}
