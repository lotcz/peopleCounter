package eu.zavadil.prototype1.persistence.dao;

import java.util.List;

public interface DaoWithLoadAll<EntityType, IdType> extends Dao<EntityType, IdType> {

    List<EntityType> loadAll();

}
