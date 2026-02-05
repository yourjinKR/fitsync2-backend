package app.fitsync.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBirthDate is a Querydsl query type for BirthDate
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBirthDate extends BeanPath<BirthDate> {

    private static final long serialVersionUID = -1131945238L;

    public static final QBirthDate birthDate = new QBirthDate("birthDate");

    public final DatePath<java.time.LocalDate> birth = createDate("birth", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> value = createDate("value", java.time.LocalDate.class);

    public QBirthDate(String variable) {
        super(BirthDate.class, forVariable(variable));
    }

    public QBirthDate(Path<? extends BirthDate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBirthDate(PathMetadata metadata) {
        super(BirthDate.class, metadata);
    }

}

