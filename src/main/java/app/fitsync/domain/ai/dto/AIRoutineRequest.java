package app.fitsync.domain.ai.dto;

import app.fitsync.domain.profile.dto.UserProfileDetailResponse;
import app.fitsync.domain.user.dto.UserHeaderInfoResponse;

/**
 * AI 운동 루틴 추천 서비스 요청<br>
 * - 사용자 정보
 */
public record AIRoutineRequest(
        UserInfo userInfo
) {
    public record UserInfo(
            UserHeaderInfoResponse userHeaderInfo,
            UserProfileDetailResponse userDetailInfo
    ) {

    }
}
