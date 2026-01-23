package app.fitsync.domain.ai.mapper;

import app.fitsync.domain.ai.dto.AILogResponse;
import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.RoutineRecommendUserMessage;
import app.fitsync.domain.ai.entity.AILog;
import app.fitsync.domain.profile.entity.UserProfile;
import app.fitsync.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AILogMapper {

    public AILogResponse toDto(AILog log) {

        return new AILogResponse(
                log.getId(),
                log.getRequestId(),
                log.getUserId(),
                log.getModel(),
                log.getFeature(),
                log.getVersion(),
                log.getInputContent(),
                log.getOutputContent(),
                log.getInputTokens(),
                log.getOutputTokens(),
                log.getRequestTime(),
                log.getResponseTime(),
                log.getElapsedMs(),
                log.getStatus(),
                log.getStatusMessage(),
                log.getUserFeedBack(),
                log.getUserFeedBackReason(),
                log.getUserAction()
        );
    }

    public RoutineRecommendUserMessage toDto(UserProfile userProfile, AIRoutineRequest request) {

        User user = userProfile.getUser();

        return new RoutineRecommendUserMessage(
                user.getAge(),
                userProfile.getWorkoutGoals(),
                userProfile.getExerciseCategories(),
                userProfile.getDisease(),
                userProfile.getHeight(),
                userProfile.getWeight(),
                userProfile.getSkeletalMuscleMass(),
                userProfile.getBodyFatMass(),
                userProfile.getBodyFatPercentage(),
                userProfile.getBmi(),
                request.splitCount()
        );
    }
}
