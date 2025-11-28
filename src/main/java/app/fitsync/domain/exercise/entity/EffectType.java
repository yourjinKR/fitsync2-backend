package app.fitsync.domain.exercise.entity;

/**
 * 운동 효과 유형
 */
public enum EffectType {
    /**
     * 근육 자극
     */
    MUSCLE,

    /**
     * 관절 관련
     */
    JOINT,

    /**
     * 심폐 지구력
     */
    CARDIO,

    /**
     * 근지구력 / 체력
     */
    ENDURANCE,

    /**
     * 관절 가동성, 유연성
     */
    MOBILITY,

    /**
     * 스트레칭, 유연성 강화
     */
    FLEXIBILITY,

    /**
     * 균형 / 코어 안정
     */
    BALANCE,

    /**
     * 재활 운동
     */
    REHABILITATION,

    /**
     * 기타
     */
    OTHER
}
