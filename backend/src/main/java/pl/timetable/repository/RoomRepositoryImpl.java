package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Room;

@Repository
public class RoomRepositoryImpl extends AbstractGenericRepositoryWithSession<Room> implements RoomRepository {

}
