package app.fitsync.domain.user.oauth.extractor;

import app.fitsync.domain.user.oauth.SocialUserInfo;
import app.fitsync.domain.user.entity.SocialProviderType;
import app.fitsync.domain.user.oauth.SocialUserInfoExtractor;
import app.fitsync.global.util.AttrUtils;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class NaverUserInfoExtractor implements SocialUserInfoExtractor {

    @Override
    public SocialProviderType supports() {
        return SocialProviderType.NAVER;
    }

    @Override
    public SocialUserInfo extract(OAuth2User oAuth2User) {
        Map<String, Object> root = oAuth2User.getAttributes();
        Map<String, Object> response = AttrUtils.asMap(root.get("response"), "response");

        String providerUserId = AttrUtils.requiredStr(response.get("id"), "response.id");
        String email = AttrUtils.str(response.get("email"));
        String name  = AttrUtils.str(response.get("name"));

        return new SocialUserInfo(SocialProviderType.NAVER, providerUserId, email, name, response);
    }
}
