package app.fitsync.domain.user.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SocialProviderType {
    NAVER("네이버"),
    GOOGLE("구글");

    private final String description;
}
