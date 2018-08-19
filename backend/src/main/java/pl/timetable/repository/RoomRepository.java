package pl.timetable.repository;

import pl.timetable.entity.Room;

public interface RoomRepository extends GenericRepository<Room> {

    public Room getRoomByNameAndNumber(String name, String number);

}
