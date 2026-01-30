package app.fitsync.domain.user.oauth;

import app.fitsync.domain.user.entity.SocialProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface SocialUserInfoExtractor {
    SocialProviderType supports();
    SocialUserInfo extract(OAuth2User oAuth2User);
}
