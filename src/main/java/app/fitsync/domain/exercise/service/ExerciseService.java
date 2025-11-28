package app.fitsync.domain.exercise.service;

import app.fitsync.domain.exercise.dto.ExerciseRequest;
import app.fitsync.domain.exercise.dto.ExerciseResponse;
import app.fitsync.domain.exercise.dto.ExerciseTargetRequest;
import app.fitsync.domain.exercise.entity.BodyDetailPart;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseTarget;
import app.fitsync.domain.exercise.entity.TargetRole;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.BodyDetailPartRepository;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExerciseService implements ExerciseServiceInterface {
    private final ExerciseMapper exerciseMapper;
    private final ExerciseRepository exerciseRepository;
    private final BodyDetailPartRepository bodyDetailPartRepository;

    @Override
    public ExerciseResponse createExercise(ExerciseRequest request) {
        List<ExerciseTargetRequest> targetRequests = request.targets();

        List<ExerciseTarget> targets = targetRequests.stream()
                .map(this::toEntity)
                .toList();

        Exercise exercise = exerciseMapper.toEntity(request, targets);
        Exercise savedExercise = exerciseRepository.save(exercise);
        Long exerciseId = savedExercise.getId();

        return new ExerciseResponse(exerciseId);
    }


    public ExerciseTarget toEntity(ExerciseTargetRequest targetRequest) {
        Long detailPartId = targetRequest.bodyDetailPartId();
        BodyDetailPart detailPart = bodyDetailPartRepository.getReferenceById(detailPartId);
        TargetRole targetRole = targetRequest.targetRole();

        return  new ExerciseTarget(detailPart, targetRole);
    }
}
