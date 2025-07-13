package renovation.batch.service.data.service;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface AbstractService<T, I> {

    List<T> findAll();

    List<T> findAll(Sort sort);

    Optional<T> findById(I id);

    /**
     * Save entity. <br />
     * <br />
     * <br />
     * If entity with the same id already exists rewrite it.
     *
     * @param entity
     * @return new created entity
     */
    T createOrUpdate(T entity);

    /**
     * Save entity. <br />
     * <br />
     * <br />
     * Throw exception if entity with given id already exists.
     *
     * @param entity
     * @param id - id of the above {@code entity}
     * @return new created entity
     */
    T createIfNotExists(T entity, I id);

    /**
     * Save entity. <br />
     * <br />
     * <br />
     * Throw exception if entity with given id doesn't exist.
     *
     * @param entity
     * @param id - id of the above {@code entity}
     * @return updated entity
     */
    T update(T entity, I id);

    /**
     * Delete entity by id.
     *
     * @param id
     * @return if entity with given id can be found then delete and return {@code true}, otherwise {@code false}
     */
    boolean delete(I id);
}
