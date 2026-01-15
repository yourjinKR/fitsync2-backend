package app.fitsync.domain.ai.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAILog is a Querydsl query type for AILog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAILog extends EntityPathBase<AILog> {

    private static final long serialVersionUID = -276507012L;

    public static final QAILog aILog = new QAILog("aILog");

    public final NumberPath<Long> elapsedMs = createNumber("elapsedMs", Long.class);

    public final StringPath feature = createString("feature");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final MapPath<String, Object, SimplePath<Object>> inputContent = this.<String, Object, SimplePath<Object>>createMap("inputContent", String.class, Object.class, SimplePath.class);

    public final NumberPath<Long> inputTokens = createNumber("inputTokens", Long.class);

    public final EnumPath<AIModel> model = createEnum("model", AIModel.class);

    public final MapPath<String, Object, SimplePath<Object>> outputContent = this.<String, Object, SimplePath<Object>>createMap("outputContent", String.class, Object.class, SimplePath.class);

    public final NumberPath<Long> outputTokens = createNumber("outputTokens", Long.class);

    public final StringPath requestId = createString("requestId");

    public final DateTimePath<java.time.LocalDateTime> requestTime = createDateTime("requestTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> responseTime = createDateTime("responseTime", java.time.LocalDateTime.class);

    public final EnumPath<CallStatus> status = createEnum("status", CallStatus.class);

    public final StringPath statusMessage = createString("statusMessage");

    public final EnumPath<UserAction> userAction = createEnum("userAction", UserAction.class);

    public final EnumPath<FeedBackStatus> userFeedBack = createEnum("userFeedBack", FeedBackStatus.class);

    public final StringPath userFeedBackReason = createString("userFeedBackReason");

    public final NumberPath<Long> userId = createNumber("ownerId", Long.class);

    public final StringPath version = createString("version");

    public QAILog(String variable) {
        super(AILog.class, forVariable(variable));
    }

    public QAILog(Path<? extends AILog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAILog(PathMetadata metadata) {
        super(AILog.class, metadata);
    }

}

