package app.fitsync.domain.exercise.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExerciseTarget is a Querydsl query type for ExerciseTarget
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExerciseTarget extends EntityPathBase<ExerciseTarget> {

    private static final long serialVersionUID = 1862293145L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExerciseTarget exerciseTarget = new QExerciseTarget("exerciseTarget");

    public final QBodyDetailPart bodyDetailPart;

    public final QExercise exercise;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<TargetRole> targetRole = createEnum("targetRole", TargetRole.class);

    public QExerciseTarget(String variable) {
        this(ExerciseTarget.class, forVariable(variable), INITS);
    }

    public QExerciseTarget(Path<? extends ExerciseTarget> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExerciseTarget(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExerciseTarget(PathMetadata metadata, PathInits inits) {
        this(ExerciseTarget.class, metadata, inits);
    }

    public QExerciseTarget(Class<? extends ExerciseTarget> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bodyDetailPart = inits.isInitialized("bodyDetailPart") ? new QBodyDetailPart(forProperty("bodyDetailPart"), inits.get("bodyDetailPart")) : null;
        this.exercise = inits.isInitialized("exercise") ? new QExercise(forProperty("exercise")) : null;
    }

}

