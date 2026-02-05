package app.fitsync.domain.profile.mapper;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.RoutineRecommendUserMessage;
import app.fitsync.domain.profile.dto.UserProfileDetailResponse;
import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserWithProfileResponse;
import app.fitsync.domain.profile.entity.UserProfile;
import app.fitsync.domain.user.dto.UserHeaderInfoResponse;
import app.fitsync.domain.user.entity.BirthDate;
import app.fitsync.domain.user.entity.User;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfile toEntity(UserProfileRequest request, User user) {
        return UserProfile.builder()
                .user(user)
                .gender(request.gender())
                .birth(new BirthDate(request.birth()))
                .workoutGoals(request.workoutGoals())
                .exerciseCategories(request.exerciseCategories())
                .disease(request.disease())
                .weight(request.weight())
                .height(request.height())
                .skeletalMuscleMass(request.skeletalMuscleMass())
                .bodyFatMass(request.bodyFatPercentage())
                .bodyFatPercentage(request.bodyFatPercentage())
                .bmi(request.bmi())
                .build();
    }

    public UserWithProfileResponse toDto(UserProfile userProfile) {

        User user = userProfile.getUser();
        UserHeaderInfoResponse userHeaderInfoResponse = toDto(user);

        UserProfileDetailResponse userProfileDetailResponse = toDetailDto(userProfile);

        return new UserWithProfileResponse(
                user.getId(),
                userHeaderInfoResponse,
                userProfile.getId(),
                userProfileDetailResponse
        );
    }

    public UserProfileDetailResponse toDetailDto(UserProfile userProfile) {

        return new UserProfileDetailResponse(
                userProfile.getGender(),
                userProfile.getBirth().getValue(),
                userProfile.getWorkoutGoals(),
                userProfile.getExerciseCategories(),
                userProfile.getDisease(),
                userProfile.getHeight(),
                userProfile.getWeight(),
                userProfile.getSkeletalMuscleMass(),
                userProfile.getBodyFatMass(),
                userProfile.getBodyFatPercentage(),
                userProfile.getBmi()
        );
    }

    public UserHeaderInfoResponse toDto(User user) {

        return new UserHeaderInfoResponse(
                user.getName(),
                user.isHidden()
        );
    }

    public RoutineRecommendUserMessage toDto(UserProfile userProfile, AIRoutineRequest request) {

        return new RoutineRecommendUserMessage(
                userProfile.getBirth().getAge(LocalDate.now()),
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
