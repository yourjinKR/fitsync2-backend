package app.fitsync.domain.exercise.entity;

/**
 * 운동이 특정 효과에 미치는 역할
 */
public enum EffectRole {
    /**
     * 주요 효과
     */
    PRIMARY,

    /**
     * 부수적 효과
     */
    SECONDARY,

    /**
     * 보조 효과
     */
    SUPPORT
}
