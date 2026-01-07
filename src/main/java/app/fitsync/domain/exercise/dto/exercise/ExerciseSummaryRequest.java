package app.fitsync.domain.exercise.dto.exercise;

public record ExerciseSummaryRequest(
        long id,
        String name,
        String detailBodyName
) {
}
