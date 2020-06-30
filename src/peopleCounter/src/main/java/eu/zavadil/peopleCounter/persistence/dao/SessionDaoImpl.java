package eu.zavadil.peopleCounter.persistence.dao;

import eu.zavadil.peopleCounter.persistence.model.Session;
import eu.zavadil.peopleCounter.persistence.repository.SessionRepository;
import org.springframework.stereotype.Component;

@Component
public class SessionDaoImpl extends DaoWithLoadAllImplBase<Session, Long, SessionRepository> implements SessionDao {

}
