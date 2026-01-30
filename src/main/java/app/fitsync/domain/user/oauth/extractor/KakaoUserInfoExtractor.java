package app.fitsync.domain.user.oauth.extractor;

import app.fitsync.domain.user.oauth.SocialUserInfo;
import app.fitsync.domain.user.entity.SocialProviderType;
import app.fitsync.domain.user.oauth.SocialUserInfoExtractor;
import app.fitsync.global.util.AttrUtils;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class KakaoUserInfoExtractor implements SocialUserInfoExtractor {

    @Override
    public SocialProviderType supports() {
        return SocialProviderType.KAKAO;
    }

    @Override
    public SocialUserInfo extract(OAuth2User oAuth2User) {
        Map<String, Object> attr = oAuth2User.getAttributes();

        String providerUserId = AttrUtils.requiredStr(attr.get("id"), "id");

        Map<String, Object> kakaoAccount = AttrUtils.asMap(attr.get("kakao_account"), "kakao_account");

        String email = AttrUtils.str(kakaoAccount.get("email"));

        String name = AttrUtils.str(kakaoAccount.get("name"));

        if (name == null) {
            Object profileObj = kakaoAccount.get("profile");
            if (profileObj instanceof Map<?, ?> profile) {
                name = AttrUtils.str(profile.get("nickname"));
            }
        }

        return new SocialUserInfo(SocialProviderType.KAKAO, providerUserId, email, name, attr);
    }
}