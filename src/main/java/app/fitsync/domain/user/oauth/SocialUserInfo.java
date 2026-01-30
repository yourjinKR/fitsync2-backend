package app.fitsync.domain.user.oauth;

import app.fitsync.domain.user.entity.SocialProviderType;
import java.util.Map;

public record SocialUserInfo(
        SocialProviderType provider,
        String providerUserId,
        String email,
        String name,
        Map<String, Object> rawAttributes
) {

    public String loginId() {
        return provider.name() + "_" + providerUserId;
    }
}
