package pl.timetable.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T extends Object> {

    T create(T entity);

    T update(T entity);

    void remove(T entity);

    T getById(Integer id);

    Optional<List<T>> findAll();
}
