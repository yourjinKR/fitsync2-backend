package app.fitsync.domain.ai.dto;


/**
 * <h3>AI 운동 루틴 추천 서비스 요청</h1><br>
 * - 사용자 ID
 * - 요청 루틴 분할 수<br>
 */
public record AIRoutineRequest(
        long userId,
        Integer splitCount
) {

}
