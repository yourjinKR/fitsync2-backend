package app.fitsync.global.util;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        List<T> content = contentQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
