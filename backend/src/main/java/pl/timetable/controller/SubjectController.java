package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.timetable.api.SubjectRequest;
import pl.timetable.dto.SubjectDto;
import pl.timetable.entity.Subject;

@RestController
@RequestMapping("/api/subject")
public class SubjectController extends GenericRestController<SubjectDto, SubjectRequest> {

    private static final Logger LOGGER = Logger.getLogger(Subject.class);

    public SubjectController() {
        super(LOGGER);
    }
}
