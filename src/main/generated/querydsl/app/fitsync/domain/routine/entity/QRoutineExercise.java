package app.fitsync.domain.routine.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoutineExercise is a Querydsl query type for RoutineExercise
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoutineExercise extends EntityPathBase<RoutineExercise> {

    private static final long serialVersionUID = -166205930L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoutineExercise routineExercise = new QRoutineExercise("routineExercise");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> displayOrder = createNumber("displayOrder", Integer.class);

    public final app.fitsync.domain.exercise.entity.QExercise exercise;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRoutine routine;

    public final ListPath<RoutineSet, QRoutineSet> routineSets = this.<RoutineSet, QRoutineSet>createList("routineSets", RoutineSet.class, QRoutineSet.class, PathInits.DIRECT2);

    public QRoutineExercise(String variable) {
        this(RoutineExercise.class, forVariable(variable), INITS);
    }

    public QRoutineExercise(Path<? extends RoutineExercise> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoutineExercise(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoutineExercise(PathMetadata metadata, PathInits inits) {
        this(RoutineExercise.class, metadata, inits);
    }

    public QRoutineExercise(Class<? extends RoutineExercise> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exercise = inits.isInitialized("exercise") ? new app.fitsync.domain.exercise.entity.QExercise(forProperty("exercise")) : null;
        this.routine = inits.isInitialized("routine") ? new QRoutine(forProperty("routine"), inits.get("routine")) : null;
    }

}

