package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.timetable.api.RoomRequest;
import pl.timetable.dto.RoomDto;
import pl.timetable.entity.Room;

@RestController
@RequestMapping("/api/room")
public class RoomController extends GenericRestController<RoomDto, RoomRequest> {

    private static final Logger LOGGER = Logger.getLogger(RoomController.class);

    public RoomController() {
        super(LOGGER);
    }
}
