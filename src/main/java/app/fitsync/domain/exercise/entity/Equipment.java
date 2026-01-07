package app.fitsync.domain.exercise.entity;

/**
 * <h2>Equipment 열거형</h2>
 *
 * <p>
 * 운동 수행에 필요한 <b>장비/도구</b>를 정의
 * </p>
 *
 * <h3>사용처</h3>
 * <ul>
 *   <li>운동 검색 필터(장비 유무, 덤벨/바벨 등)</li>
 *   <li>홈트/헬스장 구분 및 추천 조건(사용 가능 장비 기반)</li>
 * </ul>
 *
 * <h3>설계 포인트</h3>
 * <ul>
 *   <li>NO_EQUIPMENT 설정을 통해 장비가 필요 없는 운동은 null 대신 해당 값으로 안전하게 관리</li>
 * </ul>
 */
public enum Equipment {
    NO_EQUIPMENT,
    DUMBBELLS,
    BARBELL,
    BENCH,
    CABLE,
    BOSU_TRAINER,
    TRX,
    BOX,
    STABILITY_BALL,
    MEDICINE_BALL,
    CONES,
    WEIGHT_MACHINES,
    PULL_UP_BAR,
    LADDER,
    HURDLES,
    KETTLE_BELLS,
    ROPE,
}
