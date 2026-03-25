package app.fitsync.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoomParticipant is a Querydsl query type for ChatRoomParticipant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoomParticipant extends EntityPathBase<ChatRoomParticipant> {

    private static final long serialVersionUID = 1219244752L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoomParticipant chatRoomParticipant = new QChatRoomParticipant("chatRoomParticipant");

    public final app.fitsync.global.QBaseEntity _super = new app.fitsync.global.QBaseEntity(this);

    public final QChatRoom chatRoom;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastReadAt = createDateTime("lastReadAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final app.fitsync.domain.user.entity.QUser user;

    public QChatRoomParticipant(String variable) {
        this(ChatRoomParticipant.class, forVariable(variable), INITS);
    }

    public QChatRoomParticipant(Path<? extends ChatRoomParticipant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoomParticipant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoomParticipant(PathMetadata metadata, PathInits inits) {
        this(ChatRoomParticipant.class, metadata, inits);
    }

    public QChatRoomParticipant(Class<? extends ChatRoomParticipant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new QChatRoom(forProperty("chatRoom")) : null;
        this.user = inits.isInitialized("user") ? new app.fitsync.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

