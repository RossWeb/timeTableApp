package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.timetable.api.CourseRequest;
import pl.timetable.dto.CourseDto;
import pl.timetable.entity.Subject;
import pl.timetable.exception.EntityDuplicateFoundException;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.service.CourseServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController extends GenericRestController<CourseDto, CourseRequest> {

    private static final Logger LOGGER = Logger.getLogger(CourseController.class);

    public CourseController() {
        super(LOGGER);
    }

    @Autowired
    private CourseServiceImpl courseService;

    @GetMapping(value = "{id}/parameters")
    public List<Subject> getParameters(@PathVariable(value = "id") int id) throws EntityNotFoundException {
        return courseService.getParameters(id);
    }

    @DeleteMapping(value = "{id}/parameters/{idParameters}")
    public void getParameters(@PathVariable(value = "id") int id, @PathVariable(value = "idParameters") int idParameters) throws EntityNotFoundException {
        courseService.deleteParameter(id, idParameters);
    }

    @PostMapping(value = "{id}/parameters/{idParameters}")
    @ResponseStatus(HttpStatus.OK)
    public void addParameters(@PathVariable(value = "id") int id, @PathVariable(value = "idParameters") int idParameters) throws EntityNotFoundException, EntityDuplicateFoundException {
        courseService.addParameter(id, idParameters);
    }
}
