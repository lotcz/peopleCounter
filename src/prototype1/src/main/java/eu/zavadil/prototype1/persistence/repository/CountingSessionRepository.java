package eu.zavadil.prototype1.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import eu.zavadil.prototype1.persistence.model.CountingSession;
import org.springframework.stereotype.Repository;

@Repository
public interface CountingSessionRepository extends CrudRepository<CountingSession, Long> {

}
