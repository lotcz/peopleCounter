package eu.zavadil.peopleCounter.persistence.dao;

import eu.zavadil.peopleCounter.persistence.model.Face;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FaceDao extends Dao<Face, Long> {

    List<Face> loadUnprocessed(Long sessionId);

    List<Face> loadFacesToMatchAgainst(Face face);

    Integer getSessionFaceCount(Long sessionId);

    Integer getSessionProcessedFaceCount(Long sessionId);

    Integer getUniqeFaceCount(Long sessionId);
}
