package app.fitsync.domain.user.oauth;

import app.fitsync.domain.user.entity.SocialProviderType;

public final class SocialProviderResolver {
    private SocialProviderResolver() {}

    public static SocialProviderType fromRegistrationId(String registrationId) {
        if (registrationId == null || registrationId.isBlank()) {
            throw new IllegalArgumentException("registrationId is empty");
        }
        return SocialProviderType.valueOf(registrationId.trim().toUpperCase());
    }
}