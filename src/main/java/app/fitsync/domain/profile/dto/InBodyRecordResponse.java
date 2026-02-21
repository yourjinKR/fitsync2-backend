package app.fitsync.domain.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Created inbody record response")
public record InBodyRecordResponse(
        @Schema(description = "Inbody record ID", example = "100")
        long id
) {
}
