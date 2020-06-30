package eu.zavadil.peopleCounter.persistence.dao;

import eu.zavadil.peopleCounter.persistence.model.Face;
import eu.zavadil.peopleCounter.persistence.model.Session;
import eu.zavadil.peopleCounter.persistence.repository.FaceRepository;
import eu.zavadil.peopleCounter.persistence.repository.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FaceDaoImpl extends DaoImplBase<Face, Long, FaceRepository> implements FaceDao {

    public List<Face> loadUnprocessed(Long sessionId) {
        return this.repository.loadUnprocessed(sessionId);
    }

    public List<Face> loadFacesToMatchAgainst(Face face) {
        return this.repository.loadFacesToMatchAgainst(face.getPicture().getSession().getId(), face.getPicture().getCreateDate());
    }

    @Override
    public Integer getSessionFaceCount(Long sessionId) {
        return this.repository.getSessionFaceCount(sessionId);
    }

    @Override
    public Integer getSessionProcessedFaceCount(Long sessionId) {
        return this.repository.getProcessedCount(sessionId);
    }

    @Override
    public Integer getUniqeFaceCount(Long sessionId) {
        return this.repository.getUniqeFaceCount(sessionId);
    }

}
