package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.timetable.api.BaseRequest;
import pl.timetable.api.BaseResponse;
import pl.timetable.entity.BaseEntity;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.service.AbstractService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GenericRestController<T1 , T2 extends BaseRequest, R1 extends BaseResponse> {

    private final Logger logger;

    @Autowired
    private AbstractService<T1, T2, R1> service;

    public GenericRestController(Logger logger) {
        this.logger = logger;
    }

    @GetMapping
    public List<T1> list() {
        return service.findAll();
    }

    @PostMapping(value = "/find")
    @ResponseStatus(HttpStatus.OK)
    public R1 find(@RequestBody T2 request) {
        logger.info("Try to find entity from request : " + request);
        return service.find(request);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody T2 request) throws EntityNotFoundException {
        logger.info("Try to createIfNotExists entity from request : " + request);
        service.create(request);
    }

    @PutMapping(value = "{id}")
    public T1 update(@RequestBody T2 request, @PathVariable(value = "id") int id) throws EntityNotFoundException {
        logger.info("Try to update entity from request : " + request+ " with id : " + id);
        return service.update(request, id);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable(value = "id") int id) throws EntityNotFoundException {
        logger.info("Try to delete entity with id : " + id);
        service.delete(id);
    }

    @GetMapping(value = "{id}")
    public T1 get(@PathVariable(value = "id") int id) throws EntityNotFoundException {
        return service.get(id);
    }

    @ExceptionHandler
    public ResponseEntity badRequestResponse(Exception ex){
        logger.error("General error", ex);
        return ResponseEntity.badRequest().build();
    }
}


