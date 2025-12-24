package app.fitsync.domain.profile.repository;

import app.fitsync.domain.profile.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}
