package app.fitsync.domain.exercise.entity;

/**
 * <h2>MetricType 열거형</h2>
 *
 * <p>
 * 운동 기록(Record/Set 기록 등)에서 사용자가 입력하거나 시스템이 계산하는
 * <b>측정 지표(Metric)</b>를 정의
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li>운동(Exercise)마다 어떤 입력값이 필요한지 정의</li>
 *   <li>세트 기록 UI에서 입력 필드를 동적으로 구성하는 기준</li>
 *   <li>통계/분석(볼륨, 수행량, 페이스 등)의 원천 데이터 타입</li>
 * </ul>
 *
 * <h3>설계 포인트</h3>
 * <ul>
 *   <li>Exercise가 MetricType Set을 가지면 "운동별 입력 스키마"를 코드로 강제할 수 있다.</li>
 *   <li>운영 중 enum 변경은 기록 데이터와 결합되므로, 추가는 비교적 안전하지만 변경/삭제는 주의가 필요</li>
 * </ul>
 */
public enum MetricType {

    /**
     * <b>중량 (Weight)</b>
     * <p>
     * 운동 수행 시 다루는 무게를 의미합니다.<br>
     * </p>
     * 단위 : kg
     */
    WEIGHT,

    /**
     * <b>횟수 (Repetitions)</b>
     * <p>
     * 운동을 반복한 횟수를 의미합니다.<br>
     * 주로 근력 운동이나 맨몸 운동에서 사용됩니다.
     * </p>
     */
    REPS,

    /**
     * <b>거리 (Distance)</b>
     * <p>
     * 이동한 거리를 의미합니다.<br>
     * 유산소 운동(러닝, 사이클 등)에서 주로 사용됩니다.
     * </p>
     * 단위 : m(meter)
     */
    DISTANCE,

    /**
     * <b>시간 (Time / Duration)</b>
     * <p>
     * 운동을 수행한 지속 시간을 의미합니다.<br>
     * 유산소 운동의 수행 시간이나, 플랭크 같은 등척성 운동의 버티는 시간을 기록합니다.
     * </p>
     * 단위 : second
     */
    TIME,

    /**
     * <b>속도 (Speed / Pace)</b>
     * <p>
     * 운동의 속도 혹은 페이스를 의미합니다.<br>
     * 보통 거리와 시간을 통해 자동 계산되지만, 트레드밀 설정 속도 등을 직접 기록할 때 사용될 수 있습니다.
     * </p>
     * 단위: 시속/km (10km/h)<br>
     */
    SPEED,

    /**
     * <b>운동 자각도 (RPE - Rating of Perceived Exertion)</b>
     * <p>
     * 사용자가 주관적으로 느끼는 운동 강도를 1~10 사이의 숫자로 기록합니다.<br>
     * 10은 실패 지점(더 이상 수행 불가능), 1은 매우 쉬움을 의미합니다.
     * </p>
     */
    RPE,

    /**
     * <b>휴식 시간 (Rest Time)</b>
     * <p>
     * 세트와 세트 사이의 휴식 시간을 초(Second) 단위로 기록합니다.
     * </p>
     */
    REST_TIME
}