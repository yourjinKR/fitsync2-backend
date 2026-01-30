package app.fitsync.domain.user.oauth;

import app.fitsync.domain.user.entity.SocialProviderType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SocialUserInfoExtractorRegistry {

    private final Map<SocialProviderType, SocialUserInfoExtractor> map;

    public SocialUserInfoExtractorRegistry(List<SocialUserInfoExtractor> extractors) {
        EnumMap<SocialProviderType, SocialUserInfoExtractor> tmp = new EnumMap<>(SocialProviderType.class);
        for (SocialUserInfoExtractor ex : extractors) {
            SocialProviderType type = ex.supports();
            if (tmp.containsKey(type)) {
                throw new IllegalStateException("Duplicate extractor for provider: " + type);
            }
            tmp.put(type, ex);
        }
        this.map = tmp;
    }

    public SocialUserInfoExtractor get(SocialProviderType type) {
        SocialUserInfoExtractor ex = map.get(type);
        if (ex == null) {
            throw new IllegalArgumentException("Unsupported social provider: " + type);
        }
        return ex;
    }
}