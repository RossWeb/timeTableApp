package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.timetable.api.CourseRequest;
import pl.timetable.dto.CourseDto;

@RestController
@RequestMapping("/api/course")
public class CourseController extends GenericRestController<CourseDto, CourseRequest> {

    private static final Logger LOGGER = Logger.getLogger(CourseController.class);

    public CourseController() {
        super(LOGGER);
    }
}
