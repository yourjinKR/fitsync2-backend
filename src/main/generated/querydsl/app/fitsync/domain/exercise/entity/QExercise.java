package app.fitsync.domain.exercise.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExercise is a Querydsl query type for Exercise
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExercise extends EntityPathBase<Exercise> {

    private static final long serialVersionUID = 233031400L;

    public static final QExercise exercise = new QExercise("exercise");

    public final app.fitsync.global.QBaseEntity _super = new app.fitsync.global.QBaseEntity(this);

    public final EnumPath<ExerciseCategory> category = createEnum("category", ExerciseCategory.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath description = createString("description");

    public final MapPath<String, Object, SimplePath<Object>> details = this.<String, Object, SimplePath<Object>>createMap("details", String.class, Object.class, SimplePath.class);

    public final SetPath<EffectType, EnumPath<EffectType>> effects = this.<EffectType, EnumPath<EffectType>>createSet("effects", EffectType.class, EnumPath.class, PathInits.DIRECT2);

    public final SetPath<Equipment, EnumPath<Equipment>> equipments = this.<Equipment, EnumPath<Equipment>>createSet("equipments", Equipment.class, EnumPath.class, PathInits.DIRECT2);

    public final BooleanPath hidden = createBoolean("hidden");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final SetPath<MetricType, EnumPath<MetricType>> requiredMetrics = this.<MetricType, EnumPath<MetricType>>createSet("requiredMetrics", MetricType.class, EnumPath.class, PathInits.DIRECT2);

    public final ListPath<ExerciseTarget, QExerciseTarget> targets = this.<ExerciseTarget, QExerciseTarget>createList("targets", ExerciseTarget.class, QExerciseTarget.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QExercise(String variable) {
        super(Exercise.class, forVariable(variable));
    }

    public QExercise(Path<? extends Exercise> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExercise(PathMetadata metadata) {
        super(Exercise.class, metadata);
    }

}

