package app.fitsync.domain.ai.controller;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import app.fitsync.domain.ai.service.AIServiceInterface;
import app.fitsync.global.exception.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AI", description = "AI recommendation API")
public class AIController {

    private final AIServiceInterface aiService;

    @PostMapping("/api/ai/routine")
    @Operation(summary = "Generate AI routine recommendation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Generation success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profile or inbody record not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<AIRoutineResponse> generateRoutine(@RequestBody AIRoutineRequest request)
            throws JsonProcessingException {
        AIRoutineResponse response = aiService.generateRoutine(request);
        return ResponseEntity.ok(response);
    }
}
