package app.fitsync.domain.user.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class BirthDate {
    private LocalDate birth;

    public BirthDate(LocalDate localDate) {
        this.birth = localDate;
    }

    public BirthDate(LocalDateTime localDateTime) {
        this.birth = (localDateTime != null) ? localDateTime.toLocalDate() : null;
    }

    public LocalDate getValue() {
        return this.birth;
    }

    public Long getAge(LocalDate now ) {
        if (this.birth == null) throw new IllegalArgumentException("생년월일 정보가 없습니다");

        return ChronoUnit.YEARS.between(birth, now);
    }
}
