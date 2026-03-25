package app.fitsync.domain.chat.repository;

import app.fitsync.domain.chat.entity.ChatRoomParticipant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {

    boolean existsByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    List<ChatRoomParticipant> findByUserId(Long userId);

    List<ChatRoomParticipant> findByChatRoomId(Long chatRoomId);

    Optional<ChatRoomParticipant> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
