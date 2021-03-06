package pl.timetable.service;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import pl.timetable.api.TimeTableRequest;
import pl.timetable.api.TimeTableResponse;
import pl.timetable.api.TimeTableResultResponse;
import pl.timetable.dto.TimeTableDto;
import pl.timetable.dto.TimeTablePagingDto;
import pl.timetable.dto.TimeTableResultDto;
import pl.timetable.entity.TimeTable;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.TimeTableRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeTableService extends AbstractService<TimeTableDto, TimeTableRequest, TimeTableResultResponse> {

    private GroupServiceImpl groupService;
    private RoomServiceImpl roomService;
    private SubjectServiceImpl subjectService;
    private TeacherServiceImpl teacherService;
    private TimeTableDescriptionService timeTableDescriptionService;
    private TimeTableRepository timeTableRepository;

    public TimeTableService(GroupServiceImpl groupService, RoomServiceImpl roomService, SubjectServiceImpl subjectService,
                            TimeTableDescriptionService timeTableDescriptionService, TimeTableRepository timeTableRepository,
                            TeacherServiceImpl teacherService) {
        this.groupService = groupService;
        this.roomService = roomService;
        this.subjectService = subjectService;
        this.timeTableDescriptionService = timeTableDescriptionService;
        this.timeTableRepository = timeTableRepository;
        this.teacherService = teacherService;
    }

    @Override
    public List<TimeTableDto> findAll() {
        List<TimeTable> timeTablelist = timeTableRepository.findAll().orElse(Collections.emptyList());
        return timeTablelist.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public TimeTableResultResponse find(TimeTableRequest request) {
        return null;
    }

    @Override
    public void create(TimeTableRequest request) throws EntityNotFoundException {

    }

    public void create(TimeTableDto timeTableDto) throws EntityNotFoundException {
        TimeTable timeTable = mapDtoToEntity(timeTableDto);
        timeTableRepository.create(timeTable);

    }

    public List<TimeTableDto> getTimeTableResult(TimeTablePagingDto pagingRequestDto) throws EntityNotFoundException {
        Integer firstResult = pagingRequestDto.getPageNumber() * pagingRequestDto.getDays() +1;
        Integer maxResult = firstResult + pagingRequestDto.getDays() ;
        return Optional.ofNullable(timeTableRepository.getTimeTableResult(
                pagingRequestDto.getId(), firstResult, maxResult, pagingRequestDto.getGroupId()))
                .orElseThrow(() -> new EntityNotFoundException(pagingRequestDto.getId(), "TimeTable")).stream()
                .map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public List<TimeTableDto> getTimeTableResultById(Integer timeTableId) throws EntityNotFoundException {
        return Optional.ofNullable(timeTableRepository.getTimeTableResult(timeTableId))
                .orElseThrow(() -> new EntityNotFoundException(timeTableId, "TimeTable")).stream()
                .map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public Integer getTimeTableResultCount(Integer timeTableId, Integer groupId, Integer days){
        return timeTableRepository.getTimeTableCount(timeTableId, groupId) / days;
    }

    @Override
    public TimeTableDto update(TimeTableRequest request, int id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {

    }

    @Override
    public TimeTableDto get(int id) throws EntityNotFoundException {
        return null;
    }

    public TimeTableDto mapEntityToDto(TimeTable timeTable){
        TimeTableDto timeTableDto = new TimeTableDto();
        timeTableDto.setDay(timeTable.getDay());
        timeTableDto.setGroup(timeTable.getGroup());
        timeTableDto.setLectureNumber(timeTable.getLectureNumber());
        timeTableDto.setRoom(timeTable.getRoom());
        timeTableDto.setTimeTableDescription(timeTable.getTimeTableDescription());
        timeTableDto.setSubject(timeTable.getSubject());
        timeTableDto.setTeacher(timeTable.getTeacher());
        return timeTableDto;
    }

    public TimeTable mapDtoToEntity(TimeTableDto timeTableDto) throws EntityNotFoundException {
        TimeTable timeTable = new TimeTable();
        timeTable.setTimeTableDescription(TimeTableDescriptionService.mapDtoToEntity(
                timeTableDescriptionService.get(timeTableDto.getTimeTableDescription().getId())));
        if(Objects.nonNull(timeTableDto.getSubject())) {
            timeTable.setSubject(SubjectServiceImpl.mapDtoToEntity(subjectService.getByName(timeTableDto.getSubject().getName())));
        }
        if(Objects.nonNull(timeTableDto.getRoom())) {
            timeTable.setRoom(RoomServiceImpl.mapDtoToEntity(
                    roomService.getRoomByNameAndNumber(timeTableDto.getRoom().getName(), timeTableDto.getRoom().getNumber())));
        }

        if(Objects.nonNull(timeTableDto.getTeacher())) {
            timeTable.setTeacher(TeacherServiceImpl.mapDtoToEntity(
                    teacherService.get(timeTableDto.getTeacher().getId())));
        }
        timeTable.setGroup(GroupServiceImpl.mapDtoToEntity(groupService.getByGroupName(timeTableDto.getGroup().getName())));
        timeTable.setDay(timeTableDto.getDay());
        timeTable.setLectureNumber(timeTableDto.getLectureNumber());
        return timeTable;
    }
}
