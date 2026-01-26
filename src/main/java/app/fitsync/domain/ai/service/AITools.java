package app.fitsync.domain.ai.service;

import app.fitsync.domain.exercise.dto.exercise.ExerciseSummaryRequest;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AITools {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Tool(description = "exercise info in database : id(pk), name, detailBodyName")
    public List<ExerciseSummaryRequest> getExerciseInfoList(ExerciseCategory category) {
        return exerciseRepository.searchAllIsNotHidden(category).stream()
                .map(exerciseMapper::toSummaryDto)
                .toList();
    }
}
