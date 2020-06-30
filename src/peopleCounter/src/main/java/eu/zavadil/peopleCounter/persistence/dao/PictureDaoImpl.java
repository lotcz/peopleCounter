package eu.zavadil.peopleCounter.persistence.dao;

import eu.zavadil.peopleCounter.persistence.model.Picture;
import eu.zavadil.peopleCounter.persistence.repository.PictureRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureDaoImpl extends DaoImplBase<Picture, Long, PictureRepository> implements PictureDao {

    @Override
    public List<Picture> loadBySessionId(Long sessionId) {
        return this.repository.findAllBySessionId(sessionId);
    }

    /**
     * Load all pictures from a session that were not processed yet.
     * @param sessionId
     * @return
     */
    @Override
    public List<Picture> loadUnprocessed(Long sessionId) {
        return this.repository.findAllBySessionIdAndProcessedFalseOrderByCreateDateAsc(sessionId);
    }

    @Override
    public Integer getSessionPictureCount(Long sessionId) {
        return this.repository.getSessionPictureCount(sessionId);
    }

    @Override
    public Integer getSessionProcessedPictureCount(Long sessionId) {
        return this.repository.getProcessedCount(sessionId);
    }

}
