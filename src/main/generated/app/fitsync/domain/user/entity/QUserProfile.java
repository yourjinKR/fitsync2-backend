package app.fitsync.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserProfile is a Querydsl query type for UserProfile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserProfile extends EntityPathBase<UserProfile> {

    private static final long serialVersionUID = 1415506427L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserProfile userProfile = new QUserProfile("userProfile");

    public final app.fitsync.global.QBaseEntity _super = new app.fitsync.global.QBaseEntity(this);

    public final NumberPath<Double> bmi = createNumber("bmi", Double.class);

    public final NumberPath<Double> bodyFatMass = createNumber("bodyFatMass", Double.class);

    public final NumberPath<Double> bodyFatPercentage = createNumber("bodyFatPercentage", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath disease = createString("disease");

    public final SetPath<app.fitsync.domain.exercise.entity.ExerciseCategory, EnumPath<app.fitsync.domain.exercise.entity.ExerciseCategory>> exerciseCategories = this.<app.fitsync.domain.exercise.entity.ExerciseCategory, EnumPath<app.fitsync.domain.exercise.entity.ExerciseCategory>>createSet("exerciseCategories", app.fitsync.domain.exercise.entity.ExerciseCategory.class, EnumPath.class, PathInits.DIRECT2);

    public final SetPath<WorkoutGoal, EnumPath<WorkoutGoal>> goals = this.<WorkoutGoal, EnumPath<WorkoutGoal>>createSet("goals", WorkoutGoal.class, EnumPath.class, PathInits.DIRECT2);

    public final NumberPath<Double> height = createNumber("height", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> skeletalMuscleMass = createNumber("skeletalMuscleMass", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public final NumberPath<Double> weight = createNumber("weight", Double.class);

    public QUserProfile(String variable) {
        this(UserProfile.class, forVariable(variable), INITS);
    }

    public QUserProfile(Path<? extends UserProfile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserProfile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserProfile(PathMetadata metadata, PathInits inits) {
        this(UserProfile.class, metadata, inits);
    }

    public QUserProfile(Class<? extends UserProfile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

