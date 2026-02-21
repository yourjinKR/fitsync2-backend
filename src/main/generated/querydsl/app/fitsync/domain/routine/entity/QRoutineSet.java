package app.fitsync.domain.routine.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoutineSet is a Querydsl query type for RoutineSet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoutineSet extends EntityPathBase<RoutineSet> {

    private static final long serialVersionUID = -2127517340L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoutineSet routineSet = new QRoutineSet("routineSet");

    public final NumberPath<Integer> displayOrder = createNumber("displayOrder", Integer.class);

    public final NumberPath<Integer> distanceM = createNumber("distanceM", Integer.class);

    public final NumberPath<Integer> durationSec = createNumber("durationSec", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> reps = createNumber("reps", Integer.class);

    public final NumberPath<Integer> restTimeSec = createNumber("restTimeSec", Integer.class);

    public final QRoutineExercise routineExercise;

    public final NumberPath<Integer> rpe = createNumber("rpe", Integer.class);

    public final NumberPath<Integer> speedKmh = createNumber("speedKmh", Integer.class);

    public final NumberPath<Integer> weightKg = createNumber("weightKg", Integer.class);

    public QRoutineSet(String variable) {
        this(RoutineSet.class, forVariable(variable), INITS);
    }

    public QRoutineSet(Path<? extends RoutineSet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoutineSet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoutineSet(PathMetadata metadata, PathInits inits) {
        this(RoutineSet.class, metadata, inits);
    }

    public QRoutineSet(Class<? extends RoutineSet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.routineExercise = inits.isInitialized("routineExercise") ? new QRoutineExercise(forProperty("routineExercise"), inits.get("routineExercise")) : null;
    }

}

