package pl.timetable.service;

import pl.timetable.api.BaseRequest;
import pl.timetable.entity.BaseEntity;

import java.util.List;

public abstract class AbstractService<T extends BaseEntity, T2 extends BaseRequest> {

    public abstract List<T> findAll();

    public abstract void create(T2 request);

    public abstract T update(T2 request, int id);

    public abstract void delete(int id);

    public abstract T get(int id);
}
