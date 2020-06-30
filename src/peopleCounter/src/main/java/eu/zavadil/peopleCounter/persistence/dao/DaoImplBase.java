package eu.zavadil.peopleCounter.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public class DaoImplBase<EntityType, IdType, RepositoryType extends CrudRepository<EntityType, IdType>> implements Dao<EntityType, IdType> {

    @Autowired
    protected RepositoryType repository;

    @Override
    public EntityType loadById(IdType id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public EntityType save(EntityType entity) {
        return this.repository.save(entity);
    }

    @Override
    public void deleteById(IdType id) {
        this.repository.deleteById(id);
    }

    @Override
    public void delete(EntityType entity) {
        this.repository.delete(entity);
    }
}
