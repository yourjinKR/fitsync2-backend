package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static app.fitsync.domain.exercise.entity.QExercise.exercise;

@RequiredArgsConstructor
public class ExerciseRepositoryQueryDSLImpl implements ExerciseRepositoryQueryDSL {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Exercise> search(Pageable pageable, ExerciseCategory category, boolean hidden) {

        List<Exercise> content = queryFactory
                .selectFrom(exercise)
                .where(
                        eqCategory(category),
                        eqHidden(hidden)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(exercise.count())
                .from(exercise)
                .where(
                        eqCategory(category),
                        eqHidden(hidden)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
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
