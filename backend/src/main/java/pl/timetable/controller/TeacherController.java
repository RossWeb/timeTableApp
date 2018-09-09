package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import pl.timetable.api.CourseRequest;
import pl.timetable.api.CourseSubjectResponse;
import pl.timetable.api.TeacherRequest;
import pl.timetable.api.TeacherResponse;
import pl.timetable.dto.TeacherDto;
import pl.timetable.entity.Subject;
import pl.timetable.entity.Teacher;
import pl.timetable.exception.EntityDuplicateFoundException;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.service.TeacherServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController extends GenericRestController<TeacherDto, TeacherRequest, TeacherResponse> {

    private static final Logger LOGGER = Logger.getLogger(TeacherController.class);

    public TeacherController() {
        super(LOGGER);
    }

    @Autowired
    private TeacherServiceImpl teacherService;

    @GetMapping(value = "{id}/parameters")
    public List<Subject> getParameters(@PathVariable(value = "id") int id) throws EntityNotFoundException {
        return teacherService.getParameters(id);
    }

    @DeleteMapping(value = "{id}/parameters/{idParameters}")
    public void getParameters(@PathVariable(value = "id") int id, @PathVariable(value = "idParameters") int idParameters) throws EntityNotFoundException {
        teacherService.deleteParameter(id, idParameters);
    }

    @PostMapping(value = "/find/{id}/parameters")
    @ResponseStatus(HttpStatus.OK)
    public CourseSubjectResponse find(@RequestBody TeacherRequest request, @PathVariable(value = "id") int id) {
        request.setId(id);
        CourseSubjectResponse response = new CourseSubjectResponse();
        response.setData(teacherService.findParameters(request));
        response.setTotalElements(teacherService.findParametersCount());
        return response;
    }


    @PostMapping(value = "{id}/parameters/{idParameters}")
    @ResponseStatus(HttpStatus.OK)
    public void addParameters(@PathVariable(value = "id") int id, @PathVariable(value = "idParameters") int idParameters) throws EntityNotFoundException, EntityDuplicateFoundException {
        teacherService.addParameter(id, idParameters);
    }
}
