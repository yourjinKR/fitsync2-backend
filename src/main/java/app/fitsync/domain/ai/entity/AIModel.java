package app.fitsync.domain.ai.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AIModel {
    GPT_4_1_MINI("gpt-4.1-mini");

    final private String name;
}
