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
        return ChronoUnit.YEARS.between(birth, now);
    }
}
