package app.fitsync.domain.exercise.entity;

import app.fitsync.global.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "exercises")
public class Exercise extends BaseEntity {

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

    @JdbcTypeCode(SqlTypes.JSON)
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
}
