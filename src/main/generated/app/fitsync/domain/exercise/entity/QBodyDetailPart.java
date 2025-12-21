package app.fitsync.domain.exercise.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBodyDetailPart is a Querydsl query type for BodyDetailPart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBodyDetailPart extends EntityPathBase<BodyDetailPart> {

    private static final long serialVersionUID = 1141771318L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBodyDetailPart bodyDetailPart = new QBodyDetailPart("bodyDetailPart");

    public final QBodyPart bodyPart;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QBodyDetailPart(String variable) {
        this(BodyDetailPart.class, forVariable(variable), INITS);
    }

    public QBodyDetailPart(Path<? extends BodyDetailPart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBodyDetailPart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBodyDetailPart(PathMetadata metadata, PathInits inits) {
        this(BodyDetailPart.class, metadata, inits);
    }

    public QBodyDetailPart(Class<? extends BodyDetailPart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bodyPart = inits.isInitialized("bodyPart") ? new QBodyPart(forProperty("bodyPart")) : null;
    }

}

