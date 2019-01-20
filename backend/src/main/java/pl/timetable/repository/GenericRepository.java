package pl.timetable.repository;

import org.hibernate.criterion.Criterion;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T extends Object> {

    T create(T entity);

    T update(T entity);

    void remove(T entity);

    T getById(Integer id);

    Optional<List<T>> findAll();

    Integer getResultSize(Criterion filter);

    Optional<List<T>> getResult(Integer first, Integer max, Criterion filter);
}
