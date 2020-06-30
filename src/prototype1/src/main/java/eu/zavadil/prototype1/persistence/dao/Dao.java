package eu.zavadil.prototype1.persistence.dao;

public interface Dao<EntityType, IdType> {

    EntityType loadById(IdType id);

    EntityType save(EntityType entity);

    void deleteById(IdType id);

    void delete(EntityType entity);

}
