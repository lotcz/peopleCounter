package eu.zavadil.peopleCounter.persistence.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DaoWithLoadAllImplBase<EntityType, IdType, RepositoryType extends CrudRepository<EntityType, IdType>> extends DaoImplBase<EntityType, IdType, RepositoryType> implements DaoWithLoadAll<EntityType, IdType> {

    @Override
    public List<EntityType> loadAll() {
        return StreamSupport.stream(this.repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

}
