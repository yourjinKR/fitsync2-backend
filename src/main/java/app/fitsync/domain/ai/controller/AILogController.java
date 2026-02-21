package app.fitsync.domain.ai.controller;

import app.fitsync.domain.ai.dto.AILogResponse;
import app.fitsync.domain.ai.service.AILogService;
import app.fitsync.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Tag(name = "AI", description = "AI log API")
public class AILogController {

    private final AILogService aiLogService;

    @GetMapping("/api/ai/log/routine/{id}")
    @Operation(summary = "Get AI routine log detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "AI log not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<AILogResponse> generateRoutine(
            @Parameter(description = "AI log ID", required = true)
            @PathVariable long id) {
        AILogResponse response = aiLogService.viewDetail(id);
        return ResponseEntity.ok(response);
    }
}
