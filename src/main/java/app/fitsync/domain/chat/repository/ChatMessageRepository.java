package app.fitsync.domain.chat.repository;

import app.fitsync.domain.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);

    Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    @Query("""
            select count(m)
            from ChatMessage m
            where m.chatRoom.id = :roomId
            and m.sender.id <> :userId
            and (:lastReadAt is null or m.createdAt > :lastReadAt)
            """)
    long countUnreadByRoomAndUser(
            @Param("roomId") Long roomId,
            @Param("userId") Long userId,
            @Param("lastReadAt") LocalDateTime lastReadAt
    );
}
