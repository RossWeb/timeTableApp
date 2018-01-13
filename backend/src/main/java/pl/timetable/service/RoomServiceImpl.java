package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.RoomRequest;
import pl.timetable.dto.RoomDto;
import pl.timetable.entity.Room;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.RoomRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl extends AbstractService<RoomDto, RoomRequest> {

    public static final Logger LOGGER = Logger.getLogger(RoomServiceImpl.class);

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<RoomDto> findAll() {
        List<Room> roomList = roomRepository.findAll().orElse(Collections.emptyList());
        return roomList.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void create(RoomRequest roomRequest) {
        roomRepository.create(mapRequestToEntity(roomRequest));
    }

    @Override
    public RoomDto update(RoomRequest roomRequest, int id) {
        Room entity = mapRequestToEntity(roomRequest);
        entity.setId(id);
        return mapEntityToDto(roomRepository.update(entity));
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        Room room = Optional.ofNullable(roomRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Room"));
        roomRepository.remove(room);
    }

    @Override
    public RoomDto get(int id) throws EntityNotFoundException {
        Room room = Optional.ofNullable(roomRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Room"));
        return mapEntityToDto(room);
    }

    private RoomDto mapEntityToDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setNumber(room.getNumber());
        return dto;
    }

    private Room mapRequestToEntity(RoomRequest roomRequest) {
        Room entity = new Room();
        entity.setName(roomRequest.getName());
        entity.setNumber(roomRequest.getNumber());
        return entity;
    }
}
