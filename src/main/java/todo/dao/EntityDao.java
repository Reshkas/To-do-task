package todo.dao;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T> {

    List<T> findAll();

    Optional<T> findById(Integer id);

    Optional<T> create(T entity);

    int update(T entity);

    int deleteById(Integer id);
}
