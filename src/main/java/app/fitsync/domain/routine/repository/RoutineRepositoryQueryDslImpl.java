package app.fitsync.domain.routine.repository;

import app.fitsync.domain.routine.entity.Routine;
import app.fitsync.global.util.QueryDslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static app.fitsync.domain.routine.entity.QRoutine.routine;

@Repository
public class RoutineRepositoryQueryDslImpl extends QueryDslRepositorySupport implements RoutineRepositoryQueryDsl {

    public RoutineRepositoryQueryDslImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public Page<Routine> search(Pageable pageable, Long ownerId, Long writerId) {

        JPAQuery<Routine> contentQuery = queryFactory
                .selectFrom(routine)
                .where(eqOwnerId(ownerId), eqWriterId(writerId));

        JPAQuery<Long> countQuery = queryFactory
                .select(routine.count())
                .from(routine)
                .where(eqOwnerId(ownerId), eqWriterId(writerId));


        return applyPagination(pageable, contentQuery, countQuery);
    }

    public BooleanExpression eqOwnerId(Long ownerId) {

        if (ownerId == null) {
            return null;
        }
        return routine.owner.id.eq(ownerId);
    }

    public BooleanExpression eqWriterId(Long writerId) {

        if (writerId == null) {
            return null;
        }
        return routine.writer.id.eq(writerId);
    }


}
