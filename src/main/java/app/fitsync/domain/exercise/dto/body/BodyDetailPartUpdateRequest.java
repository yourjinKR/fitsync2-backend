package app.fitsync.domain.exercise.dto.body;

public record BodyDetailPartUpdateRequest(
        Long detailPartId,
        Long partId
) {
}
