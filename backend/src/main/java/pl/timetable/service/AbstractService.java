package pl.timetable.service;

import pl.timetable.api.BaseRequest;
import pl.timetable.entity.BaseEntity;
import pl.timetable.exception.EntityNotFoundException;

import java.util.List;

public abstract class AbstractService<T, T2 extends BaseRequest> {

    public abstract List<T> findAll();

    public abstract void create(T2 request) throws EntityNotFoundException;

    public abstract T update(T2 request, int id) throws EntityNotFoundException;

    public abstract void delete(int id) throws EntityNotFoundException;

    public abstract T get(int id) throws EntityNotFoundException;

}
