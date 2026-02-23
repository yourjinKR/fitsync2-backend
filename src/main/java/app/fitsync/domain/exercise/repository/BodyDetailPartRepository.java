package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.dto.body.BodyDetailPartListResponse;
import app.fitsync.domain.exercise.entity.BodyDetailPart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyDetailPartRepository extends JpaRepository<BodyDetailPart, Long> {
    @Query("""
        select new app.fitsync.domain.exercise.dto.body.BodyDetailPartListResponse(
            d.id, d.name, p.name
        )
        from BodyDetailPart d
        join d.bodyPart p
        order by d.id asc
    """)
    List<BodyDetailPartListResponse> findAllListResponses();
}
