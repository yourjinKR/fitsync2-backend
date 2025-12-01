package app.fitsync.domain.exercise.service;

import app.fitsync.domain.exercise.dto.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.ExerciseListResponse;
import app.fitsync.domain.exercise.dto.ExerciseRequest;
import app.fitsync.domain.exercise.dto.ExerciseResponse;
import app.fitsync.domain.exercise.dto.ExerciseTargetRequest;
import app.fitsync.domain.exercise.entity.BodyDetailPart;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseTarget;
import app.fitsync.domain.exercise.exception.ExerciseErrorCode;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.BodyDetailPartRepository;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.global.exception.RestApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExerciseService implements ExerciseServiceInterface {
    private final ExerciseMapper exerciseMapper;
    private final ExerciseRepository exerciseRepository;
    private final BodyDetailPartRepository bodyDetailPartRepository;

    @Override
    @Transactional
    public ExerciseResponse createExercise(ExerciseRequest request) {
        List<ExerciseTargetRequest> targetRequests = request.targets();

        List<BodyDetailPart> bodyDetailParts = targetRequests.stream()
                .map(ExerciseTargetRequest::bodyDetailPartId)
                .map(bodyDetailPartRepository::getReferenceById)
                .toList();

        List<ExerciseTarget> targets = exerciseMapper.toEntities(bodyDetailParts, targetRequests);

        Exercise exercise = exerciseMapper.toEntity(request, targets);
        Exercise savedExercise = exerciseRepository.save(exercise);
        Long exerciseId = savedExercise.getId();

        return new ExerciseResponse(exerciseId);
    }

    @Override
    public Page<ExerciseListResponse> getExerciseList(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Exercise> exercises = exerciseRepository.findAll(pageable);

        return exercises.map(exerciseMapper::toListDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ExerciseDetailResponse findExercise(Long id) {
        Exercise exercise = exerciseRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RestApiException(ExerciseErrorCode.NOT_FOUND));

        return exerciseMapper.toDto(exercise);
    }
}
