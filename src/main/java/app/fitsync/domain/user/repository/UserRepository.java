package app.fitsync.domain.user.repository;

import app.fitsync.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByLoginId(String loginId);

    Optional<User> findByLoginIdAndHiddenIsFalse(String loginId);

    Optional<User> findByLoginIdAndIsSocial(String loginId, Boolean isSocial);
}
