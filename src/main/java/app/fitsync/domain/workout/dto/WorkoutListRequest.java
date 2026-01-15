package app.fitsync.domain.workout.dto;

public record WorkoutListRequest(
        long ownerId
        // TODO : 검색 조건 추가
        // writerId
        // date ~ date
        // category
) {

}
