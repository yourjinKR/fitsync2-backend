package app.fitsync.domain.profile.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInBodyRecord is a Querydsl query type for InBodyRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInBodyRecord extends EntityPathBase<InBodyRecord> {

    private static final long serialVersionUID = 1363590883L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInBodyRecord inBodyRecord = new QInBodyRecord("inBodyRecord");

    public final NumberPath<Double> bmi = createNumber("bmi", Double.class);

    public final NumberPath<Double> bodyFatMass = createNumber("bodyFatMass", Double.class);

    public final NumberPath<Double> bodyFatPercentage = createNumber("bodyFatPercentage", Double.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> skeletalMuscleMass = createNumber("skeletalMuscleMass", Double.class);

    public final QUserProfile userProfile;

    public final NumberPath<Double> weight = createNumber("weight", Double.class);

    public QInBodyRecord(String variable) {
        this(InBodyRecord.class, forVariable(variable), INITS);
    }

    public QInBodyRecord(Path<? extends InBodyRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInBodyRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInBodyRecord(PathMetadata metadata, PathInits inits) {
        this(InBodyRecord.class, metadata, inits);
    }

    public QInBodyRecord(Class<? extends InBodyRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userProfile = inits.isInitialized("userProfile") ? new QUserProfile(forProperty("userProfile"), inits.get("userProfile")) : null;
    }

}

