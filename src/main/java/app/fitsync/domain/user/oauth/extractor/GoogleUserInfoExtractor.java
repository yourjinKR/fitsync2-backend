package app.fitsync.domain.user.oauth.extractor;

import app.fitsync.domain.user.oauth.SocialUserInfo;
import app.fitsync.domain.user.entity.SocialProviderType;
import app.fitsync.domain.user.oauth.SocialUserInfoExtractor;
import app.fitsync.global.util.AttrUtils;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Component
public class GoogleUserInfoExtractor implements SocialUserInfoExtractor {

    @Override
    public SocialProviderType supports() {
        return SocialProviderType.GOOGLE;
    }

    @Override
    public SocialUserInfo extract(OAuth2User oAuth2User) {
        Map<String, Object> attr = oAuth2User.getAttributes();

        String providerUserId = AttrUtils.requiredStr(attr.get("sub"), "sub");
        String email = AttrUtils.str(attr.get("email"));
        String name  = AttrUtils.str(attr.get("name"));

        return new SocialUserInfo(SocialProviderType.GOOGLE, providerUserId, email, name, attr);
    }
}

