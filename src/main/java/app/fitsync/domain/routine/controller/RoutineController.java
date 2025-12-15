package app.fitsync.domain.routine.controller;

import app.fitsync.domain.routine.dto.routine.RoutineRequest;
import app.fitsync.domain.routine.dto.routine.RoutineResponse;
import app.fitsync.domain.routine.service.RoutineServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class RoutineController {

    private final RoutineServiceInterface routineService;

    @PostMapping("/api/routine")
    public ResponseEntity<RoutineResponse> create(@Valid @RequestBody RoutineRequest request) {

        RoutineResponse response = routineService.create(request);
        return ResponseEntity.ok(response);
    }
}
