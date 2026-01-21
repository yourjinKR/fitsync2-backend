package app.fitsync.domain.ai.service;

import app.fitsync.domain.ai.dto.AILogResponse;
import app.fitsync.domain.ai.entity.AILog;
import app.fitsync.domain.ai.exception.AILogErrorCode;
import app.fitsync.domain.ai.mapper.AILogMapper;
import app.fitsync.domain.ai.repository.AILogRepository;
import app.fitsync.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AILogService {

    private final AILogRepository aiLogRepository;
    private final AILogMapper aiLogMapper;

    public AILogResponse viewDetail(long id) {

        AILog log = aiLogRepository.findById(id)
                .orElseThrow(() -> new RestApiException(AILogErrorCode.NOT_FOUND));

        return aiLogMapper.toDto(log);
    }

}
