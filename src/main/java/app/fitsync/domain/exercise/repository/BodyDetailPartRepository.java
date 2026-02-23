package app.fitsync.domain.exercise.repository;

import app.fitsync.domain.exercise.entity.BodyDetailPart;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyDetailPartRepository extends JpaRepository<BodyDetailPart, Long> {
    @EntityGraph(attributePaths = "bodyPart")
    List<BodyDetailPart> findAllByOrderByIdAsc();
}
