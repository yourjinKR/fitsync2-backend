package app.fitsync.domain.chat.repository;

import app.fitsync.domain.chat.entity.ChatRoom;
import app.fitsync.domain.chat.entity.ChatRoomType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
            select cr
            from ChatRoom cr
            where cr.type = :type
            and exists (select 1 from ChatRoomParticipant p1 where p1.chatRoom = cr and p1.user.id = :userA)
            and exists (select 1 from ChatRoomParticipant p2 where p2.chatRoom = cr and p2.user.id = :userB)
            and (select count(p3) from ChatRoomParticipant p3 where p3.chatRoom = cr) = 2
            """)
    Optional<ChatRoom> findDirectRoomByUsers(
            @Param("type") ChatRoomType type,
            @Param("userA") Long userA,
            @Param("userB") Long userB
    );
}
