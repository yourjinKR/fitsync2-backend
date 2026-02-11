package app.fitsync.domain.profile.repository;

import app.fitsync.domain.profile.entity.InBodyRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InBodyRecordRepository extends JpaRepository<InBodyRecord, Long> {

    Optional<InBodyRecord> findTop1ByUserProfile_IdOrderByCreatedAtDesc(Long userProfileId);
    List<InBodyRecord> findByUserProfileIdOrderByCreatedAtDesc(Long userProfileId);
}
