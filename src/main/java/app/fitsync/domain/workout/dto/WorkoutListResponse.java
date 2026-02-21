package app.fitsync.domain.workout.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "운동 기록 목록 항목")
public record WorkoutListResponse(
        long id,
        LocalDateTime createdAt
) {
}
