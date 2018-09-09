package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.timetable.api.HoursLectureRequest;
import pl.timetable.api.HoursLectureResponse;
import pl.timetable.dto.HoursLectureDto;

@RestController
@RequestMapping("/api/hoursLecture")
public class HoursLectureController extends GenericRestController<HoursLectureDto, HoursLectureRequest, HoursLectureResponse> {
    private static final Logger LOGGER = Logger.getLogger(HoursLectureController.class);

    public HoursLectureController() {
        super(LOGGER);
    }
}