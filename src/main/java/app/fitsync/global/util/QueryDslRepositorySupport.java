package app.fitsync.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class QueryDslRepositorySupport {

    protected final JPAQueryFactory queryFactory;

    public QueryDslRepositorySupport(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public <T> Page<T> applyPagination(
            Pageable pageable,
            JPAQuery<T> contentQuery,
            JPAQuery<Long> countQuery) {

        applySorting(pageable, contentQuery);

        List<T> content = contentQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private <T> void applySorting(Pageable pageable, JPAQuery<T> contentQuery) {
        if (pageable.getSort().isUnsorted() || contentQuery.getMetadata().getJoins().isEmpty()) {
            return;
        }

        String alias = contentQuery.getMetadata().getJoins().getFirst().getTarget().toString();
        PathBuilder<Object> entityPath = new PathBuilder<>(Object.class, alias);

        for (Sort.Order sortOrder : pageable.getSort()) {
            Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;
            contentQuery.orderBy(
                    new OrderSpecifier<>(
                            direction,
                            entityPath.getComparable(sortOrder.getProperty(), Comparable.class)
                    )
            );
        }
    }
}
