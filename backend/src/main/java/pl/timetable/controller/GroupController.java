package pl.timetable.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.timetable.api.GroupRequest;
import pl.timetable.entity.Group;

@RestController
@RequestMapping("/api/group")
public class GroupController extends GenericRestController<Group, GroupRequest> {
}
