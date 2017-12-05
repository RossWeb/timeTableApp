package pl.timetable.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.timetable.api.BaseRequest;
import pl.timetable.entity.BaseEntity;
import pl.timetable.service.AbstractService;

import java.util.List;

public class GenericRestController<T1 extends BaseEntity, T2 extends BaseRequest> {

    @Autowired
    private AbstractService<T1, T2> service;

    @GetMapping
    public List<T1> list() {
        return service.findAll();
    }

    @PostMapping
    public void create(@RequestBody T2 request) {
        service.create(request);
    }

    @PutMapping(value = "{id}")
    public T1 update(@RequestBody T2 request, @PathVariable(value = "id") int id) {
        return service.update(request, id);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable(value = "id") int id) {
        service.delete(id);
    }

    @GetMapping(value = "{id}")
    public T1 get(@PathVariable(value = "id") int id) {
        return service.get(id);
    }
}
