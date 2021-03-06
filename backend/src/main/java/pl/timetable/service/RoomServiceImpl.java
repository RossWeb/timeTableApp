package pl.timetable.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.RoomRequest;
import pl.timetable.api.RoomResponse;
import pl.timetable.dto.RoomDto;
import pl.timetable.entity.Room;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.RoomRepository;

import javax.persistence.criteria.Predicate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl extends AbstractService<RoomDto, RoomRequest, RoomResponse> {

    public static final Logger LOGGER = Logger.getLogger(RoomServiceImpl.class);

    private RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomDto> findAll() {
        List<Room> roomList = roomRepository.findAll().orElse(Collections.emptyList());
        return roomList.stream().map(RoomServiceImpl::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public RoomResponse find(RoomRequest request) {
        RoomResponse roomResponse = new RoomResponse();
        Integer first = request.getPageNumber() * request.getSize();
        Integer max = first + request.getSize();
        List<Room> roomList = roomRepository.getResult(first, max, getFilter(request)).orElse(Collections.emptyList());
        roomResponse.setData(roomList.stream().map(RoomServiceImpl::mapEntityToDto).collect(Collectors.toList()));
        roomResponse.setTotalElements(roomRepository.getResultSize(getFilter(request)));
        return roomResponse;
    }

    private Criterion getFilter(RoomRequest request){
        Conjunction conjunction = Restrictions.conjunction();
        if(StringUtils.isNotEmpty(request.getData().getName())){
            conjunction.add(Restrictions.like("name", "%" + request.getData().getName() +"%"));
        }
        if(StringUtils.isNotEmpty(request.getData().getNumber())){
            conjunction.add(Restrictions.like("number","%" + request.getData().getNumber() + "%"));
        }
        return conjunction;

    }

    @Override
    public void create(RoomRequest roomRequest) {
        roomRepository.create(mapRequestToEntity(roomRequest));
    }

    public RoomDto getRoomByNameAndNumber(String name, String number) throws EntityNotFoundException {
        return mapEntityToDto(Optional.ofNullable(roomRepository.getRoomByNameAndNumber(name, number))
                .orElseThrow(() -> new EntityNotFoundException(name, "Room")));

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

    public static RoomDto mapEntityToDto(Room room) {
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

    public static Room mapDtoToEntity(RoomDto roomDto){
        Room room = new Room();
        room.setName(roomDto.getName());
        room.setNumber(roomDto.getNumber());
        room.setId(roomDto.getId());
        return room;
    }
}
