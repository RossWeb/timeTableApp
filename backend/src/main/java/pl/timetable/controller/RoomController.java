package pl.timetable.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.timetable.api.RoomRequest;
import pl.timetable.entity.Room;

@RestController
@RequestMapping("/api/room")
public class RoomController extends GenericRestController<Room, RoomRequest> {
}
