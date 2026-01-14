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

    public static final QWorkout workout = new QWorkout("workout");

    public final app.fitsync.global.QBaseEntity _super = new app.fitsync.global.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final ListPath<WorkoutExercise, QWorkoutExercise> workoutExercises = this.<WorkoutExercise, QWorkoutExercise>createList("workoutExercises", WorkoutExercise.class, QWorkoutExercise.class, PathInits.DIRECT2);

    public QWorkout(String variable) {
        super(Workout.class, forVariable(variable));
    }

    public QWorkout(Path<? extends Workout> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorkout(PathMetadata metadata) {
        super(Workout.class, metadata);
    }

}

