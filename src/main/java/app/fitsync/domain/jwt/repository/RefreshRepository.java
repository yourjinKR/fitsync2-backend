package app.fitsync.domain.jwt.repository;

import app.fitsync.domain.jwt.domain.RefreshEntity;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refreshToken);

    @Transactional
    void deleteByRefresh(String refresh);

    @Transactional
    void deleteByLoginId(String loginId);

    @Transactional
    void deleteByCreatedDateBefore(LocalDateTime createdDate);
}
