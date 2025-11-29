package app.fitsync.domain.exercise.entity;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exercises")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise extends BaseEntity {

    @Getter
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
    private Map<String, Object> details;

    @Column(name = "hidden")
    private boolean hidden = false;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseTarget> targets = new ArrayList<>();

    @ElementCollection(targetClass = EffectType.class)
    @CollectionTable(
            name = "exercise_effects",
            joinColumns = @JoinColumn(name = "exercise_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "effect_type")
    private Set<EffectType> effects = new HashSet<>();

    public void addTarget(ExerciseTarget target) {
        this.targets.add(target);
        target.setExercise(this);
    }

    public void addAllTargets(List<ExerciseTarget> targets) {
        for (ExerciseTarget target : targets) {
            addTarget(target);
        }
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
