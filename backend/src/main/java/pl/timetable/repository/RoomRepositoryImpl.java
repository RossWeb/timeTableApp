package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Room;

@Repository
public class RoomRepositoryImpl extends AbstractGenericRepositoryWithSession<Room> implements RoomRepository {

    public Room getRoomByNameAndNumber(String name, String number) {
        return (Room) getSession().getNamedQuery("findRoomByNameAndNumber")
                .setParameter("name", name)
                .setParameter("number", number).uniqueResult();
    }

}
