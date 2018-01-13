package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.timetable.api.GroupRequest;
import pl.timetable.dto.GroupDto;
import pl.timetable.entity.Group;
import pl.timetable.service.AbstractService;

@RestController
@RequestMapping("/api/group")
public class GroupController extends GenericRestController<GroupDto, GroupRequest> {

    private static final Logger LOGGER = Logger.getLogger(GroupController.class);

    public GroupController() {
        super(LOGGER);
    }
}
