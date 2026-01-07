package app.fitsync.domain.ai.dto;

import app.fitsync.domain.profile.dto.UserProfileDetailResponse;
import app.fitsync.domain.user.dto.UserHeaderInfoResponse;

/**
 * <h3>AI 운동 루틴 추천 서비스 요청</h1><br>
 * - 요청 루틴 분할 수<br>
 * - 사용자 정보
 */
public record AIRoutineRequest(
        Integer splitCount,
        UserInfo userInfo
) {
    public record UserInfo(
            UserHeaderInfoResponse userHeaderInfo,
            UserProfileDetailResponse userDetailInfo
    ) {

    }
}
