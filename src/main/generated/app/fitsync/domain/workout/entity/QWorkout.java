package app.fitsync.domain.workout.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkout is a Querydsl query type for Workout
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkout extends EntityPathBase<Workout> {

    private static final long serialVersionUID = -55807746L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkout workout = new QWorkout("workout");

    public final app.fitsync.global.QBaseEntity _super = new app.fitsync.global.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final app.fitsync.domain.user.entity.QUser owner;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<WorkoutExercise, QWorkoutExercise> workoutExercises = this.<WorkoutExercise, QWorkoutExercise>createList("workoutExercises", WorkoutExercise.class, QWorkoutExercise.class, PathInits.DIRECT2);

    public final app.fitsync.domain.user.entity.QUser writer;

    public QWorkout(String variable) {
        this(Workout.class, forVariable(variable), INITS);
    }

    public QWorkout(Path<? extends Workout> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkout(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkout(PathMetadata metadata, PathInits inits) {
        this(Workout.class, metadata, inits);
    }

    public QWorkout(Class<? extends Workout> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new app.fitsync.domain.user.entity.QUser(forProperty("owner"), inits.get("owner")) : null;
        this.writer = inits.isInitialized("writer") ? new app.fitsync.domain.user.entity.QUser(forProperty("writer"), inits.get("writer")) : null;
    }

}

