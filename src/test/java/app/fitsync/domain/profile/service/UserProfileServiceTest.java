package app.fitsync.domain.profile.service;

import app.fitsync.domain.exercise.entity.ExerciseCategory;
import app.fitsync.domain.profile.dto.InBodyRecordMeRequest;
import app.fitsync.domain.profile.dto.InBodyRecordResponse;
import app.fitsync.domain.profile.dto.InBodyStatisticsResponse;
import app.fitsync.domain.profile.dto.UserProfileRequest;
import app.fitsync.domain.profile.dto.UserProfileResponse;
import app.fitsync.domain.profile.dto.UserWithProfileResponse;
import app.fitsync.domain.profile.entity.InBodyRecord;
import app.fitsync.domain.profile.entity.UserProfile;
import app.fitsync.domain.profile.entity.WorkoutGoal;
import app.fitsync.domain.profile.exception.InBodyException;
import app.fitsync.domain.profile.exception.UserProfileException;
import app.fitsync.domain.profile.mapper.UserProfileMapper;
import app.fitsync.domain.profile.repository.InBodyRecordRepository;
import app.fitsync.domain.profile.repository.UserProfileRepository;
import app.fitsync.domain.user.CurrentUserProvider;
import app.fitsync.domain.user.dto.UserHeaderInfoResponse;
import app.fitsync.domain.user.entity.Gender;
import app.fitsync.domain.user.entity.User;
import app.fitsync.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private InBodyRecordRepository inBodyRecordRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private InBodyStatisticsCalculator inBodyStatisticsCalculator;

    private UserProfileService service() {
        return new UserProfileService(
                currentUserProvider,
                userProfileRepository,
                inBodyRecordRepository,
                userProfileMapper,
                inBodyStatisticsCalculator
        );
    }

    private UserProfileRequest profileRequest() {
        return new UserProfileRequest(
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                Set.of(WorkoutGoal.MUSCLE_GAIN),
                Set.of(ExerciseCategory.FITNESS),
                null,
                175.0,
                70.0,
                35.0,
                15.0,
                21.4,
                22.9
        );
    }

    @Test
    @DisplayName("TS-PROFILE-001: 동일 사용자 프로필 2회 생성 시 DUPLICATE")
    void createProfile_duplicate_throwsDuplicate() {
        UserProfileService userProfileService = service();

        User user = User.builder().id(1L).loginId("tester").roleType(app.fitsync.domain.user.entity.UserRoleType.MEMBER).isSocial(false).build();
        when(currentUserProvider.getUser()).thenReturn(user);
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(org.mockito.Mockito.mock(UserProfile.class)));

        assertThatThrownBy(() -> userProfileService.create(profileRequest()))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(UserProfileException.DUPLICATE);
    }

    @Test
    @DisplayName("TS-PROFILE-002: 프로필 조회 시 최신 인바디 포함 응답")
    void viewProfile_returnsProfileWithLatestInBody() {
        UserProfileService userProfileService = service();

        UserProfile profile = org.mockito.Mockito.mock(UserProfile.class);
        InBodyRecord latest = org.mockito.Mockito.mock(InBodyRecord.class);
        UserWithProfileResponse mapped = new UserWithProfileResponse(
                1L,
                new UserHeaderInfoResponse("tester", false),
                10L,
                org.mockito.Mockito.mock(app.fitsync.domain.profile.dto.UserProfileDetailResponse.class)
        );

        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(profile.getId()).thenReturn(10L);
        when(inBodyRecordRepository.findTop1ByUserProfile_IdOrderByCreatedAtDesc(10L)).thenReturn(Optional.of(latest));
        when(userProfileMapper.toDto(profile, latest)).thenReturn(mapped);

        UserWithProfileResponse response = userProfileService.view(1L);

        assertThat(response.profileId()).isEqualTo(10L);
        verify(userProfileMapper).toDto(profile, latest);
    }

    @Test
    @DisplayName("TS-PROFILE-003: 인바디 없는 프로필 조회 시 NOT_FOUND_PROFILE_ID")
    void viewProfile_withoutInBody_throwsNotFoundProfileId() {
        UserProfileService userProfileService = service();

        UserProfile profile = org.mockito.Mockito.mock(UserProfile.class);
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(profile.getId()).thenReturn(10L);
        when(inBodyRecordRepository.findTop1ByUserProfile_IdOrderByCreatedAtDesc(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.view(1L))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(InBodyException.NOT_FOUND_PROFILE_ID);
    }

    @Test
    @DisplayName("TS-PROFILE-004: 인바디 통계는 레코드 2건 이상일 때 증감 계산")
    void viewInBodyStatistics_withTwoOrMoreRecords_calculatesSummary() {
        UserProfileService userProfileService = service();

        InBodyRecord r1 = InBodyRecord.builder().id(1L).weight(70.0).skeletalMuscleMass(35.0).bodyFatMass(15.0).bodyFatPercentage(20.0).bmi(22.0).createdAt(LocalDateTime.now()).build();
        InBodyRecord r2 = InBodyRecord.builder().id(2L).weight(68.0).skeletalMuscleMass(34.0).bodyFatMass(16.0).bodyFatPercentage(22.0).bmi(23.0).createdAt(LocalDateTime.now().minusDays(7)).build();
        List<InBodyRecord> records = List.of(r1, r2);

        InBodyStatisticsResponse.InBodySummary summary = new InBodyStatisticsResponse.InBodySummary(2.0, 2.94, 1.0, 2.94, -1.0, -6.25);

        when(inBodyRecordRepository.findByUserProfileIdOrderByCreatedAtDesc(10L)).thenReturn(records);
        when(inBodyStatisticsCalculator.calculateSummary(records)).thenReturn(summary);
        when(userProfileMapper.toDto(r1)).thenReturn(new InBodyStatisticsResponse.InBodyTrendElement(r1.getCreatedAt(), 70.0, 35.0, 15.0, 20.0, 22.0));
        when(userProfileMapper.toDto(r2)).thenReturn(new InBodyStatisticsResponse.InBodyTrendElement(r2.getCreatedAt(), 68.0, 34.0, 16.0, 22.0, 23.0));

        InBodyStatisticsResponse response = userProfileService.viewInBodyStatics(10L);

        assertThat(response.summary()).isEqualTo(summary);
        assertThat(response.trends()).hasSize(2);
        verify(inBodyStatisticsCalculator).calculateSummary(records);
    }

    @Test
    @DisplayName("인바디 생성 시 프로필이 존재하면 저장 성공")
    void createInBody_success() {
        UserProfileService userProfileService = service();

        InBodyRecordMeRequest request = new InBodyRecordMeRequest(70.0, 35.0, 15.0, 20.0, 22.0);
        UserProfile profile = org.mockito.Mockito.mock(UserProfile.class);
        InBodyRecord mapped = InBodyRecord.builder().build();
        InBodyRecord saved = InBodyRecord.builder().id(55L).build();

        when(currentUserProvider.getUserId()).thenReturn(1L);
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(userProfileMapper.toEntity(request, profile)).thenReturn(mapped);
        when(inBodyRecordRepository.save(mapped)).thenReturn(saved);

        InBodyRecordResponse response = userProfileService.createMyInBody(request);

        assertThat(response.id()).isEqualTo(55L);
    }

    @Test
    @DisplayName("인바디 생성 시 프로필이 없으면 NOT_FOUND")
    void createInBody_profileMissing_throwsNotFound() {
        UserProfileService userProfileService = service();
        InBodyRecordMeRequest request = new InBodyRecordMeRequest(70.0, 35.0, 15.0, 20.0, 22.0);

        when(currentUserProvider.getUserId()).thenReturn(99L);
        when(userProfileRepository.findByUserId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.createMyInBody(request))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(UserProfileException.NOT_FOUND);
    }

    @Test
    @DisplayName("프로필 생성 성공")
    void createProfile_success() {
        UserProfileService userProfileService = service();

        User user = User.builder().id(1L).loginId("tester").roleType(app.fitsync.domain.user.entity.UserRoleType.MEMBER).isSocial(false).build();
        UserProfile mapped = UserProfile.builder().id(10L).build();
        UserProfile saved = UserProfile.builder().id(10L).build();

        when(currentUserProvider.getUser()).thenReturn(user);
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userProfileMapper.toEntity(org.mockito.ArgumentMatchers.any(UserProfileRequest.class), org.mockito.ArgumentMatchers.eq(user))).thenReturn(mapped);
        when(userProfileRepository.save(mapped)).thenReturn(saved);

        UserProfileResponse response = userProfileService.create(profileRequest());

        assertThat(response.id()).isEqualTo(10L);
    }
}

