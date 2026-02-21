package app.fitsync.domain.exercise.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBodyPart is a Querydsl query type for BodyPart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBodyPart extends EntityPathBase<BodyPart> {

    private static final long serialVersionUID = -120769083L;

    public static final QBodyPart bodyPart = new QBodyPart("bodyPart");

    public final ListPath<BodyDetailPart, QBodyDetailPart> detailParts = this.<BodyDetailPart, QBodyDetailPart>createList("detailParts", BodyDetailPart.class, QBodyDetailPart.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QBodyPart(String variable) {
        super(BodyPart.class, forVariable(variable));
    }

    public QBodyPart(Path<? extends BodyPart> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBodyPart(PathMetadata metadata) {
        super(BodyPart.class, metadata);
    }

}

