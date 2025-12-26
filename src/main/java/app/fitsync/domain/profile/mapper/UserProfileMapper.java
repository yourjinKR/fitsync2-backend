package app.fitsync.domain.profile.mapper;

import app.fitsync.domain.profile.dto.UserProfileDetailResponse;
import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.entity.UserProfile;
import app.fitsync.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfile toEntity(UserProfileRequest request, User user) {
        return UserProfile.builder()
                .user(user)
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

    public UserProfileDetailResponse toDto(UserProfile profile) {

        User user = profile.getUser();

        UserProfileDetailResponse.User userResponse = new UserProfileDetailResponse.User(
                user.getName(),
                user.getAge(),
                user.isHidden()
        );

        return new UserProfileDetailResponse(
                profile.getId(),
                userResponse,
                profile.getWorkoutGoals(),
                profile.getExerciseCategories(),
                profile.getDisease(),
                profile.getHeight(),
                profile.getWeight(),
                profile.getSkeletalMuscleMass(),
                profile.getBodyFatMass(),
                profile.getBodyFatPercentage(),
                profile.getBmi()
        );
    }
}
