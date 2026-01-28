package app.fitsync.global.config;

import app.fitsync.domain.jwt.repository.RefreshRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduleConfig {

    private final RefreshRepository refreshRepository;

    public ScheduleConfig(RefreshRepository refreshRepository) {
        this.refreshRepository = refreshRepository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void refreshEntityTtlSchedule() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(8);
        refreshRepository.deleteByCreatedDateBefore(cutoff);
    }

}
