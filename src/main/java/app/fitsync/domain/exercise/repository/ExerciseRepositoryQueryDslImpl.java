package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.global.util.QueryDslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static app.fitsync.domain.exercise.entity.QExercise.exercise;

@Repository
public class ExerciseRepositoryQueryDslImpl extends QueryDslRepositorySupport implements ExerciseRepositoryQueryDsl {

    public ExerciseRepositoryQueryDslImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public Page<Exercise> search(Pageable pageable, ExerciseCategory category, boolean hidden) {

        JPAQuery<Exercise> contentQuery = queryFactory
                .selectFrom(exercise)
                .where(eqCategory(category), eqHidden(hidden));

        JPAQuery<Long> countQuery = queryFactory
                .select(exercise.count())
                .from(exercise)
                .where(eqCategory(category), eqHidden(hidden));

        return applyPagination(pageable, contentQuery, countQuery);
    }

    @Override
    public List<Exercise> searchAllIsNotHidden(ExerciseCategory category) {

        return queryFactory
                .selectFrom(exercise)
                .where(
                        eqCategory(category),
                        eqHidden(false)
                ).fetch();
    }

    private BooleanExpression eqCategory(ExerciseCategory category) {
        if (category == null) {
            return null;
        }
        return exercise.category.eq(category);
    }

    private BooleanExpression eqHidden(boolean hidden) {
        return exercise.hidden.eq(hidden);
    }
}
