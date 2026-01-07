package app.fitsync.domain.exercise.entity;

/**
 * <h2>ExerciseCategory 열거형</h2>
 *
 * <p>
 * 운동(Exercise)의 <b>대분류 카테고리</b>를 나타내는 열거형
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>운동 목록 필터링/검색/분류 기준 제공</li>
 *   <li>루틴 추천(카테고리 기반 추천/선호 카테고리 매칭)의 핵심 키</li>
 *   <li>DB 저장 시 EnumType.STRING으로 저장하는 경우, 가독성과 확장성에 유리</li>
 * </ul>
 */
public enum ExerciseCategory {
    /**
     * 헬스, 근력 중심 운동
     */
    FITNESS,

    /**
     * 크로스핏
     */
    CROSSFIT,

    /**
     * 요가
     */
    YOGA,

    /**
     * 필라테스
     */
    PILATES,

    /**
     * 재활, 물리치료용 운동
     */
    REHAB
}
