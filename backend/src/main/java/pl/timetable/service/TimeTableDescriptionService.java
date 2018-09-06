package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.BaseResponse;
import pl.timetable.api.TimeTableDescritpionRequest;
import pl.timetable.dto.TimeTableDescriptionDto;
import pl.timetable.entity.Subject;
import pl.timetable.entity.TimeTableDescription;
import pl.timetable.enums.TimeTableDescriptionStatus;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.TimeTableDescriptionRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TimeTableDescriptionService extends AbstractService<TimeTableDescriptionDto, TimeTableDescritpionRequest, BaseResponse> {

    public static final Logger LOGGER = Logger.getLogger(TimeTableDescriptionService.class);

    private TimeTableDescriptionRepository timeTableDescriptionRepository;

    public void setTimeTableDescriptionRepository(TimeTableDescriptionRepository timeTableDescriptionRepository) {
        this.timeTableDescriptionRepository = timeTableDescriptionRepository;
    }

    public TimeTableDescriptionService(TimeTableDescriptionRepository timeTableDescriptionRepository) {
        this.timeTableDescriptionRepository = timeTableDescriptionRepository;
    }

    @Override
    public List<TimeTableDescriptionDto> findAll() {
        List<TimeTableDescription> timeTableDescriptionList = timeTableDescriptionRepository.findAll().orElse(Collections.emptyList());
        return timeTableDescriptionList.stream().map(TimeTableDescriptionService::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public BaseResponse find(TimeTableDescritpionRequest request) {
        return null;
    }

    @Override
    public void create(TimeTableDescritpionRequest request) throws EntityNotFoundException {
        //czy potrzebne?
    }

    @Override
    public TimeTableDescriptionDto update(TimeTableDescritpionRequest request, int id) throws EntityNotFoundException {
        //czy potrzebne
        return null;
    }

    public void updateStatus(Integer timeTableDescriptionId, TimeTableDescriptionStatus status) throws EntityNotFoundException {
        TimeTableDescription timeTableDescription = Optional.ofNullable(timeTableDescriptionRepository.getById(timeTableDescriptionId))
                .orElseThrow(() -> new EntityNotFoundException(timeTableDescriptionId, "TimeTableDescription"));
        timeTableDescription.setTimeTableDescriptionStatus(status);
        timeTableDescriptionRepository.update(timeTableDescription);
    }

    public TimeTableDescription create(String name, LocalDateTime startedDate){
        TimeTableDescription timeTableDescription = new TimeTableDescription();
        timeTableDescription.setCreatedDate(LocalDateTime.now());
        timeTableDescription.setStartedDate(startedDate);
        timeTableDescription.setName(name);
        timeTableDescriptionRepository.create(timeTableDescription);
        return timeTableDescription;
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        TimeTableDescription entity = Optional.ofNullable(timeTableDescriptionRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "TimeTableDescription"));
        timeTableDescriptionRepository.remove(entity);
    }

    @Override
    public TimeTableDescriptionDto get(int id) throws EntityNotFoundException {
        TimeTableDescription timeTableDescription = Optional.ofNullable(timeTableDescriptionRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "TimeTableDescription"));
        return mapEntityToDto(timeTableDescription);
    }

    public static TimeTableDescriptionDto mapEntityToDto(TimeTableDescription timeTableDescription) {
        TimeTableDescriptionDto timeTableDescriptionDto =  new TimeTableDescriptionDto(timeTableDescription.getName(),
                timeTableDescription.getId(), timeTableDescription.getCreatedDate(), timeTableDescription.getStartedDate());
        timeTableDescriptionDto.setStatus(timeTableDescription.getTimeTableDescriptionStatus());
        return timeTableDescriptionDto;
    }

    public static TimeTableDescription mapDtoToEntity(TimeTableDescriptionDto timeTableDescriptionDto){
        TimeTableDescription timeTableDescription = new TimeTableDescription();
        timeTableDescription.setName(timeTableDescriptionDto.getName());
        timeTableDescription.setCreatedDate(timeTableDescriptionDto.getCreatedDate());
        timeTableDescription.setId(timeTableDescriptionDto.getId());
        timeTableDescription.setTimeTableDescriptionStatus(timeTableDescriptionDto.getStatus());
        return timeTableDescription;
    }

}
