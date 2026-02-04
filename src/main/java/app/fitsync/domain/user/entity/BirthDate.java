package app.fitsync.domain.user.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class BirthDate {
    public static BirthDate EMPTY = new BirthDate(null);
    private LocalDateTime birth;

    public BirthDate(LocalDateTime localDateTime) {
        this.birth = localDateTime;
    }

    public Long getAge() {
        LocalDateTime now = LocalDateTime.now();

        if (this.birth == null) throw new IllegalArgumentException("생년월일 정보가 없습니다");

        return ChronoUnit.YEARS.between(birth, now);
    }
}
