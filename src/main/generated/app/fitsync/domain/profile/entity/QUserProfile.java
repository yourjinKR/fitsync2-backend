package app.fitsync.domain.profile.entity;

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

    private static final long serialVersionUID = 1988683443L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserProfile userProfile = new QUserProfile("userProfile");

    public final app.fitsync.global.QBaseEntity _super = new app.fitsync.global.QBaseEntity(this);

    public final app.fitsync.domain.user.entity.QBirthDate birth;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath disease = createString("disease");

    public final SetPath<app.fitsync.domain.exercise.entity.ExerciseCategory, EnumPath<app.fitsync.domain.exercise.entity.ExerciseCategory>> exerciseCategories = this.<app.fitsync.domain.exercise.entity.ExerciseCategory, EnumPath<app.fitsync.domain.exercise.entity.ExerciseCategory>>createSet("exerciseCategories", app.fitsync.domain.exercise.entity.ExerciseCategory.class, EnumPath.class, PathInits.DIRECT2);

    public final EnumPath<app.fitsync.domain.user.entity.Gender> gender = createEnum("gender", app.fitsync.domain.user.entity.Gender.class);

    public final NumberPath<Double> height = createNumber("height", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<InBodyRecord, QInBodyRecord> inBodyRecords = this.<InBodyRecord, QInBodyRecord>createSet("inBodyRecords", InBodyRecord.class, QInBodyRecord.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final app.fitsync.domain.user.entity.QUser user;

    public final SetPath<WorkoutGoal, EnumPath<WorkoutGoal>> workoutGoals = this.<WorkoutGoal, EnumPath<WorkoutGoal>>createSet("workoutGoals", WorkoutGoal.class, EnumPath.class, PathInits.DIRECT2);

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
        this.birth = inits.isInitialized("birth") ? new app.fitsync.domain.user.entity.QBirthDate(forProperty("birth")) : null;
        this.user = inits.isInitialized("user") ? new app.fitsync.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

