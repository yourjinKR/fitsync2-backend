package app.fitsync.domain.exercise.service;

import app.fitsync.domain.exercise.dto.exercise.ExerciseDetailResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseListResponse;
import app.fitsync.domain.exercise.dto.exercise.ExerciseRequest;
import app.fitsync.domain.exercise.dto.exercise.ExerciseResponse;
import app.fitsync.domain.exercise.dto.target.ExerciseTargetDeleteRequest;
import app.fitsync.domain.exercise.dto.target.ExerciseTargetRequest;
import app.fitsync.domain.exercise.dto.exercise.ExerciseUpdateRequest;
import app.fitsync.domain.exercise.dto.target.ExerciseTargetUpdateRequest;
import app.fitsync.domain.exercise.entity.BodyDetailPart;
import app.fitsync.domain.exercise.entity.Exercise;
import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.exercise.entity.ExerciseTarget;
import app.fitsync.domain.exercise.exception.ExerciseErrorCode;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.BodyDetailPartRepository;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.exercise.repository.ExerciseTargetRepository;
import app.fitsync.global.DeleteType;
import app.fitsync.global.exception.RestApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@NullMarked
@Service
@RequiredArgsConstructor
public class ExerciseService implements ExerciseServiceInterface {
    private final ExerciseMapper exerciseMapper;
    private final ExerciseRepository exerciseRepository;
    private final BodyDetailPartRepository bodyDetailPartRepository;
    private final ExerciseTargetRepository exerciseTargetRepository;

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
    public Page<ExerciseListResponse> getExerciseList(
            Pageable pageable,
            ExerciseCategory category,
            boolean hidden
    ) {
        Page<Exercise> exercises = exerciseRepository.search(pageable, category, hidden);

        return exercises.map(exerciseMapper::toListDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ExerciseDetailResponse findExercise(Long id) {
        Exercise exercise = exerciseRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RestApiException(ExerciseErrorCode.NOT_FOUND, id));

        return exerciseMapper.toDto(exercise);
    }

    @Override
    @Transactional
    public ExerciseResponse updateExercise(Long id, ExerciseUpdateRequest request) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ExerciseErrorCode.NOT_FOUND, id));

        List<ExerciseTargetRequest> targetRequests = request.newTargets();
        List<ExerciseTarget> newTargets = targetRequests.stream()
                .map(this::addNewTarget)
                .toList();
        exercise.addAllTargets(newTargets);

        List<ExerciseTargetUpdateRequest> updateTargets = request.updateTargets();
        updateTargets.forEach(this::updateExerciseTarget);

        List<ExerciseTargetDeleteRequest> deleteRequests = request.deleteTargets();
        deleteRequests.forEach(this::deleteTarget);

        exercise.updateFrom(request);

        return new ExerciseResponse(exercise.getId());
    }

    private void updateExerciseTarget(ExerciseTargetUpdateRequest targetRequest) {
        Long targetId = targetRequest.id();

        ExerciseTarget target = exerciseTargetRepository.findById(targetId)
                .orElseThrow(() -> new RestApiException(ExerciseErrorCode.TARGET_NOT_FOUNT, targetId));

        target.updateFrom(targetRequest);
    }

    private ExerciseTarget addNewTarget(ExerciseTargetRequest targetRequest) {
        Long detailPartId = targetRequest.bodyDetailPartId();
        BodyDetailPart bodyDetail = bodyDetailPartRepository.getReferenceById(detailPartId);

        return new ExerciseTarget(bodyDetail, targetRequest.targetRole());
    }

    private void deleteTarget(ExerciseTargetDeleteRequest request) {
        ExerciseTarget target = exerciseTargetRepository.getReferenceById(request.id());
        exerciseTargetRepository.delete(target);
    }

    @Override
    @Transactional
    public ExerciseResponse deleteExercise(Long id, DeleteType deleteType) {
        Exercise exercise = findById(id);

        if (deleteType == DeleteType.SOFT) {
            exercise.hide();
            exerciseRepository.save(exercise);
        }
        else if (deleteType == DeleteType.HARD) {
            exerciseRepository.delete(exercise);
        }
        else {
            throw new IllegalArgumentException();
        }

        return new ExerciseResponse(exercise.getId());
    }

    public Exercise findById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ExerciseErrorCode.NOT_FOUND, id));
    }
}
