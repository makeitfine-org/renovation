package renovation.batch.service.data.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import renovation.batch.service.data.service.AbstractService;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public abstract class AbstractServiceImpl<T, I> implements AbstractService<T, I> {

    @Getter(AccessLevel.PROTECTED)
    private JpaRepository<T, I> repository;

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Optional<T> findById(I id) {
        return repository.findById(id);
    }

    @Override
    public T createOrUpdate(T t) {
        log.info("{} create/update: {}", t.getClass().getSimpleName(), t);

        return repository.save(t);
    }

    @Override
    public T createIfNotExists(T t, I id) {
        if (repository.existsById(id)) {
            throw new IllegalArgumentException("entity exists with id = " + id);
        }

        log.info("{} create if not exists: {}", t.getClass().getSimpleName(), t);

        return repository.save(t);
    }

    @Override
    public T update(T t, I id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Not found element with id = " + id);
        }

        log.info("{} update: {}", t.getClass().getSimpleName(), t);

        return repository.save(t);
    }

    @Override
    public boolean delete(I id) {
        log.info("delete entity with id = {}", id);

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
