package eu.zavadil.peopleCounter.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import eu.zavadil.peopleCounter.persistence.model.Session;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

}
