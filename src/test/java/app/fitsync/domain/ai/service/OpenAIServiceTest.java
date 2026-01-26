package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AIRoutineRequest;
import app.fitsync.domain.ai.dto.AIRoutineResponse;
import app.fitsync.domain.ai.dto.RoutineRecommendUserMessage;
import app.fitsync.domain.ai.entity.AIModel;
import app.fitsync.domain.ai.entity.OpenAiMessageConverter;
import app.fitsync.domain.ai.mapper.AILogMapper;
import app.fitsync.domain.exercise.mapper.ExerciseMapper;
import app.fitsync.domain.exercise.repository.ExerciseRepository;
import app.fitsync.domain.profile.entity.UserProfile;
import app.fitsync.domain.profile.mapper.UserProfileMapper;
import app.fitsync.domain.profile.repository.UserProfileRepository;
import app.fitsync.global.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenAIServiceTest {

    @Mock ExerciseRepository exerciseRepository;
    @Mock UserProfileRepository userProfileRepository;
    @Mock ExerciseMapper exerciseMapper;
    @Mock UserProfileMapper userProfileMapper;
    @Mock AILogWriter aiLogWriter;
    @Mock ChatClient chatClient;

    OpenAIService openAIService;

    @BeforeEach
    void setUp() {
        openAIService = new OpenAIService(
                exerciseRepository,
                userProfileRepository,
                exerciseMapper,
                userProfileMapper,
                aiLogWriter,
                chatClient
        );
    }

    @Test
    @DisplayName(
            "루틴 추천 성공 시: AI 응답 JSON을 AIRoutineResponse로 파싱하고, "
                    + "aiLogWriter.success가 토큰 정보와 함께 호출된다"
    )
    void generateRoutine_success_logsSuccessAndReturnsParsedResult() throws Exception {
        // given
        long userId = 1L;

        AIRoutineRequest request = mock(AIRoutineRequest.class);
        when(request.userId()).thenReturn(userId);

        UserProfile profile = mock(UserProfile.class);
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

        RoutineRecommendUserMessage userMessageDto = mock(RoutineRecommendUserMessage.class);
        when(userProfileMapper.toDto(profile, request)).thenReturn(userMessageDto);

        OpenAiMessageConverter generator = mock(OpenAiMessageConverter.class);
        when(generator.getInputJsonOf()).thenReturn(Map.of("dummy", "input"));
        when(generator.getListOf()).thenReturn(List.of()); // Prompt 생성용

        // static factory mocking
        try (MockedStatic<OpenAiMessageConverter> mocked =
                     Mockito.mockStatic(OpenAiMessageConverter.class)) {
            mocked.when(() -> OpenAiMessageConverter.routineRecommendOf(userMessageDto))
                    .thenReturn(generator);

            // ===== ChatResponse mocking (Spring AI 1.1.2 타입 흐름) =====
            String jsonContent =
                    """
                    {
                      "result": [
                        {
                          "name": "DAY 1",
                          "routineExercises": [
                            {
                              "exerciseId": 1,
                              "exerciseName": "Squat",
                              "routineSets": [
                                {
                                  "weightKg": 100,
                                  "reps": 5,
                                  "distanceM": 0,
                                  "durationSec": 0,
                                  "speedKmh": 0,
                                  "rpe": 8,
                                  "restTimeSec": 120
                                }
                              ]
                            }
                          ]
                        }
                      ]
                    }
                    """;

            Usage usage = mock(Usage.class);
            when(usage.getPromptTokens()).thenReturn(10);
            when(usage.getCompletionTokens()).thenReturn(20);

            ChatResponseMetadata metadata = mock(ChatResponseMetadata.class);
            when(metadata.getUsage()).thenReturn(usage);

            AssistantMessage assistantMessage = mock(AssistantMessage.class);
            when(assistantMessage.getText()).thenReturn(jsonContent);

            Generation generation = mock(Generation.class);
            when(generation.getOutput()).thenReturn(assistantMessage);

            ChatResponse response = mock(ChatResponse.class);
            when(response.getMetadata()).thenReturn(metadata);
            when(response.getResult()).thenReturn(generation);

            // ===== chatClient 체인 모킹 =====
            ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
            ChatClient.ChatClientRequestSpec toolSpec = mock(ChatClient.ChatClientRequestSpec.class);
            ChatClient.CallResponseSpec callSpec = mock(ChatClient.CallResponseSpec.class);

            when(chatClient.prompt(any(Prompt.class))).thenReturn(requestSpec);
            when(requestSpec.tools(any())).thenReturn(toolSpec);
            when(toolSpec.call()).thenReturn(callSpec);
            when(callSpec.chatResponse()).thenReturn(response);

            // when
            AIRoutineResponse actual = openAIService.generateRoutine(request);

            // then: JSON 파싱 성공 검증
            assertThat(actual).isNotNull();
            assertThat(actual.result()).hasSize(1);
            assertThat(actual.result().get(0).name()).isEqualTo("DAY 1");
            assertThat(actual.result().get(0).routineExercises().get(0).exerciseName())
                    .isEqualTo("Squat");

            // then: 로그 검증
            ArgumentCaptor<String> requestIdCaptor = ArgumentCaptor.forClass(String.class);

            verify(aiLogWriter).init(
                    requestIdCaptor.capture(),
                    eq(userId),
                    eq(AIModel.GPT_4_1_MINI),
                    eq("ROUTINE_RECOMMEND"),
                    eq("0.0.1"),
                    any()
            );

            String requestId = requestIdCaptor.getValue();
            verify(aiLogWriter).success(eq(requestId), anyMap(), eq(10L), eq(20L));
            verify(aiLogWriter, never()).failure(anyString(), any(), anyString());
        }
    }

    @Test
    @DisplayName(
            "프로필이 없으면: RestApiException을 던지고, "
                    + "aiLogWriter.init/success/failure 로그는 호출되지 않는다"
    )
    void generateRoutine_profileNotFound_throwsAndDoesNotCallInit() {
        // given
        long userId = 99L;

        AIRoutineRequest request = mock(AIRoutineRequest.class);
        when(request.userId()).thenReturn(userId);

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> openAIService.generateRoutine(request))
                .isInstanceOf(RestApiException.class);

        verify(aiLogWriter, never()).init(anyString(), anyLong(), any(), anyString(), anyString(), any());
        verify(aiLogWriter, never()).success(anyString(), anyMap(), anyLong(), anyLong());
        verify(aiLogWriter, never()).failure(anyString(), any(), anyString());
    }

    @Test
    @DisplayName("외부(OpenAI) 호출 중 예외 발생 시: aiLogWriter.failure를 남기고, 예외를 그대로 다시 던진다")
    void generateRoutine_chatClientThrows_logsFailureAndRethrows() throws Exception {
        // given
        long userId = 1L;

        AIRoutineRequest request = mock(AIRoutineRequest.class);
        when(request.userId()).thenReturn(userId);

        UserProfile profile = mock(UserProfile.class);
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

        RoutineRecommendUserMessage userMessageDto = mock(RoutineRecommendUserMessage.class);
        when(userProfileMapper.toDto(profile, request)).thenReturn(userMessageDto);

        OpenAiMessageConverter generator = mock(OpenAiMessageConverter.class);
        when(generator.getInputJsonOf()).thenReturn(Map.of("dummy", "input"));
        when(generator.getListOf()).thenReturn(List.of());

        try (MockedStatic<OpenAiMessageConverter> mocked =
                     Mockito.mockStatic(OpenAiMessageConverter.class)) {
            mocked.when(() -> OpenAiMessageConverter.routineRecommendOf(userMessageDto))
                    .thenReturn(generator);

            ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
            ChatClient.ChatClientRequestSpec toolSpec = mock(ChatClient.ChatClientRequestSpec.class);

            when(chatClient.prompt(any(Prompt.class))).thenReturn(requestSpec);
            when(requestSpec.tools(any())).thenReturn(toolSpec);

            RuntimeException boom = new RuntimeException("OpenAI down!");
            when(toolSpec.call()).thenThrow(boom);

            // when / then
            assertThatThrownBy(() -> openAIService.generateRoutine(request))
                    .isSameAs(boom);

            ArgumentCaptor<String> requestIdCaptor = ArgumentCaptor.forClass(String.class);
            verify(aiLogWriter).init(
                    requestIdCaptor.capture(),
                    eq(userId),
                    eq(AIModel.GPT_4_1_MINI),
                    eq("ROUTINE_RECOMMEND"),
                    eq("0.0.1"),
                    any()
            );

            verify(aiLogWriter).failure(eq(requestIdCaptor.getValue()), isNull(), contains("OpenAI down!"));
            verify(aiLogWriter, never()).success(anyString(), anyMap(), anyLong(), anyLong());
        }
    }

}
