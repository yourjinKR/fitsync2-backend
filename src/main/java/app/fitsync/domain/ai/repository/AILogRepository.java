package app.fitsync.domain.ai.repository;

import app.fitsync.domain.ai.entity.AILog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AILogRepository extends JpaRepository<AILog, Long> {

    Optional<AILog> findByRequestId(String requestId);
}
