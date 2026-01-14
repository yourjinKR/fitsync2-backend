package app.fitsync.domain.workout.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkoutSet is a Querydsl query type for WorkoutSet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkoutSet extends EntityPathBase<WorkoutSet> {

    private static final long serialVersionUID = -416134524L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkoutSet workoutSet = new QWorkoutSet("workoutSet");

    public final NumberPath<Integer> displayOrder = createNumber("displayOrder", Integer.class);

    public final NumberPath<Integer> distanceM = createNumber("distanceM", Integer.class);

    public final NumberPath<Integer> durationSec = createNumber("durationSec", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final NumberPath<Integer> reps = createNumber("reps", Integer.class);

    public final NumberPath<Integer> restTimeSec = createNumber("restTimeSec", Integer.class);

    public final NumberPath<Integer> rpe = createNumber("rpe", Integer.class);

    public final NumberPath<Integer> speedKmh = createNumber("speedKmh", Integer.class);

    public final NumberPath<Integer> weightKg = createNumber("weightKg", Integer.class);

    public final QWorkoutExercise workoutExercise;

    public QWorkoutSet(String variable) {
        this(WorkoutSet.class, forVariable(variable), INITS);
    }

    public QWorkoutSet(Path<? extends WorkoutSet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkoutSet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkoutSet(PathMetadata metadata, PathInits inits) {
        this(WorkoutSet.class, metadata, inits);
    }

    public QWorkoutSet(Class<? extends WorkoutSet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.workoutExercise = inits.isInitialized("workoutExercise") ? new QWorkoutExercise(forProperty("workoutExercise"), inits.get("workoutExercise")) : null;
    }

}

