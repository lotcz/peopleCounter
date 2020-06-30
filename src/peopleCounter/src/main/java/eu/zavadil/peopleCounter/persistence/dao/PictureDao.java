package eu.zavadil.peopleCounter.persistence.dao;

import eu.zavadil.peopleCounter.persistence.model.Face;
import eu.zavadil.peopleCounter.persistence.model.Picture;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PictureDao extends Dao<Picture, Long> {

    List<Picture> loadBySessionId(Long sessionId);

    List<Picture> loadUnprocessed(Long sessionId);

    Integer getSessionPictureCount(Long sessionId);

    Integer getSessionProcessedPictureCount(Long sessionId);
}
