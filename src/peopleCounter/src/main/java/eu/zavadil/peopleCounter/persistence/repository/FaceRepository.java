package eu.zavadil.peopleCounter.persistence.repository;

import eu.zavadil.peopleCounter.persistence.model.Face;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FaceRepository extends CrudRepository<Face, Long> {

    @Query("SELECT f FROM Face f WHERE f.picture.session.id = :sessionId AND f.processed = false")
    List<Face> loadUnprocessed(@Param("sessionId") Long sessionId);

    @Query("SELECT f FROM Face f WHERE f.picture.session.id = :sessionId AND f.picture.createDate < :date")
    List<Face> loadFacesToMatchAgainst(@Param("sessionId") Long sessionId, @Param("date") Date date);

    @Query("SELECT count(f) FROM Face f WHERE f.picture.session.id = :sessionId")
    Integer getSessionFaceCount(@Param("sessionId") Long sessionId);

    @Query("SELECT count(f) FROM Face f WHERE f.picture.session.id = :sessionId AND f.processed = true")
    Integer getProcessedCount(@Param("sessionId") Long sessionId);

    @Query("SELECT count(f) FROM Face f WHERE f.picture.session.id = :sessionId AND f.matched = false")
    Integer getUniqeFaceCount(@Param("sessionId") Long sessionId);
}
