package app.fitsync.domain.exercise.entity;

/**
 * <h2>TargetRole 열거형</h2>
 *
 * <p>
 * 특정 운동이 자극하는 부위를 <b>주 타겟(Main) / 보조 타겟(Sub)</b>으로 구분합니다.
 * </p>
 *
 * <h3>사용처</h3>
 * <ul>
 *   <li>{@link ExerciseTarget}에서 타겟 부위의 역할 구분</li>
 *   <li>추천/분석 로직에서 "주 타겟 가중치" 등의 규칙 적용 근거</li>
 * </ul>
 *
 * <h3>확장 포인트</h3>
 * <ul>
 *   <li>추후 "TERTIARY(3차)" 또는 "STABILIZER(안정근)" 등 세분화 가능</li>
 * </ul>
 */
public enum TargetRole {
    MAIN,
    SUB
}