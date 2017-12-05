package pl.timetable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.RoomRequest;
import pl.timetable.entity.Room;
import pl.timetable.repository.RoomRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomServiceImpl extends AbstractService<Room, RoomRequest> {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> findAll() {
        Optional<List<Room>> roomList = roomRepository.findAll();
        return roomList.orElse(Collections.emptyList());
    }

    @Override
    public void create(RoomRequest roomRequest) {
        roomRepository.create(mapRequestToEntity(roomRequest));
    }

    @Override
    public Room update(RoomRequest roomRequest, int id) {
        Room entity = mapRequestToEntity(roomRequest);
        entity.setId(id);
        return roomRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        Room entity = get(id);
        roomRepository.remove(entity);
    }

    @Override
    public Room get(int id) {
        return roomRepository.getById(id);
    }

    private Room mapRequestToEntity(RoomRequest roomRequest){
        Room entity = new Room();
        entity.setName(roomRequest.getName());
        entity.setNumber(roomRequest.getNumber());
        return entity;
    }
}
