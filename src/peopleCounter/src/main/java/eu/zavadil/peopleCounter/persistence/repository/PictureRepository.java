package eu.zavadil.peopleCounter.persistence.repository;

import eu.zavadil.peopleCounter.persistence.model.Picture;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends CrudRepository<Picture, Long> {

    List<Picture> findAllBySessionId(Long sessionId);

    List<Picture> findAllBySessionIdAndProcessedFalseOrderByCreateDateAsc(Long sessionId);

    @Query("SELECT count(p) FROM Picture p WHERE (p.session.id = :sessionId) ")
    Integer getSessionPictureCount(@Param("sessionId") Long sessionId);

    @Query("SELECT count(p) FROM Picture p WHERE p.session.id = :sessionId AND p.processed = true")
    Integer getProcessedCount(Long sessionId);

}
