package app.fitsync.domain.exercise.entity;

public enum EffectType {
    STRENGTH("근력"),
    ENDURANCE("지구력"),
    CARDIO("심폐"),
    FLEXIBILITY("유연성"),
    MOBILITY("가동성/고관절");

    // 생성자, getter 생략
    private final String description;

    EffectType(String description) {
        this.description = description;
    }
}
