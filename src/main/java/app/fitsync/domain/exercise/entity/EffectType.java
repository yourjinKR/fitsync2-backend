package app.fitsync.domain.exercise.entity;

/**
 * <h2>EffectType 열거형</h2>
 *
 * <p>
 * 운동이 기대하는 <b>효과(효능/목표 성격)</b>를 분류하기 위한 열거형
 * </p>
 *
 * <h3>예시</h3>
 * <ul>
 *   <li>근력(Strength), 지구력(Endurance), 심폐(Cardio) 등</li>
 * </ul>
 *
 * <h3>사용 의도</h3>
 * <ul>
 *   <li>운동 추천/검색에서 "목적 기반" 필터링의 기준</li>
 *   <li>UI에서 태그/뱃지 표시에 사용 (description)</li>
 * </ul>
 */
public enum EffectType {

    STRENGTH("근력"),
    ENDURANCE("지구력"),
    CARDIO("심폐"),
    FLEXIBILITY("유연성"),
    MOBILITY("가동성/고관절");

    /**
     * 사용자 표시용 설명(한글 라벨)
     * - DB에는 enum name을 저장하고,
     * - 화면 표시는 description을 사용하는 식으로 역할을 분리할 수 있습니다.
     */
    private final String description;

    EffectType(String description) {
        this.description = description;
    }
}
