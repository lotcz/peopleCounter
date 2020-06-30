package eu.zavadil.prototype1.persistence.repository;

import eu.zavadil.prototype1.persistence.model.Face;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceRepository extends CrudRepository<Face, Long> {

}
