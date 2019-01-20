package pl.timetable.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.*;
import pl.timetable.dto.CourseDto;
import pl.timetable.dto.HoursLectureDto;
import pl.timetable.entity.Course;
import pl.timetable.entity.HoursLecture;
import pl.timetable.entity.Subject;
import pl.timetable.exception.EntityDuplicateFoundException;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.CourseRepository;
import pl.timetable.repository.HoursLectureRepository;
import pl.timetable.repository.SubjectRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HoursLectureService extends AbstractService<HoursLectureDto, HoursLectureRequest, HoursLectureResponse> {

    public static final Logger LOGGER = Logger.getLogger(HoursLectureService.class);

    @Autowired
    private HoursLectureRepository hoursLectureRepository;

    @Override
    public List<HoursLectureDto> findAll() {
        List<HoursLecture> entityHours = hoursLectureRepository.findAll().orElse(Collections.emptyList());
        return entityHours.stream().map(hour -> {
            return mapEntityToDto(hour);
        }).collect(Collectors.toList());
    }

    @Override
    public HoursLectureResponse find(HoursLectureRequest request) {
        HoursLectureResponse response = new HoursLectureResponse();
        Integer first = request.getPageNumber() * request.getSize();
        Integer max = first + request.getSize();
        List<HoursLecture> hoursList = hoursLectureRepository.getResult(first, max, getFilter(request)).orElse(Collections.emptyList());
        response.setData(hoursList.stream().map(hour -> {
            return mapEntityToDto(hour);
        }).collect(Collectors.toList()));
        response.setTotalElements(hoursLectureRepository.getResultSize(getFilter(request)));
        return response;
    }

    private Criterion getFilter(HoursLectureRequest request){
        Conjunction conjunction = Restrictions.conjunction();
        if(Objects.nonNull(request.getData().getPosition())){
            conjunction.add(Restrictions.like("position", "%"+request.getData().getPosition()+"%"));
        }
        return conjunction;

    }

    @Override
    public void create(HoursLectureRequest request) throws EntityNotFoundException {
        HoursLecture hoursLecture = new HoursLecture();
        hoursLecture.setStartLectureTime(LocalTime.parse(request.getStartLectureTime(), DateTimeFormatter.ISO_LOCAL_TIME));
        hoursLecture.setPosition(request.getPosition());
        hoursLectureRepository.create(hoursLecture);
    }

    @Override
    public HoursLectureDto update(HoursLectureRequest request, int id) throws EntityNotFoundException {
        HoursLecture hoursLecture = new HoursLecture();
        hoursLecture.setId(id);
        hoursLecture.setPosition(request.getPosition());
        hoursLecture.setStartLectureTime(LocalTime.parse(request.getStartLectureTime(), DateTimeFormatter.ISO_LOCAL_TIME));
        return mapEntityToDto(hoursLectureRepository.update(hoursLecture));
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        HoursLecture hoursLecture = Optional.ofNullable(hoursLectureRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "HoursLecture"));
        hoursLectureRepository.remove(hoursLecture);
    }

    @Override
    public HoursLectureDto get(int id) throws EntityNotFoundException {
        HoursLecture hoursLecture = Optional.ofNullable(hoursLectureRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "HoursLecture"));
        return mapEntityToDto(hoursLecture);
    }

    public static HoursLectureDto mapEntityToDto(HoursLecture hoursLecture) {
        HoursLectureDto dto = new HoursLectureDto();
        dto.setStartLectureTime(hoursLecture.getStartLectureTime());
        dto.setId(hoursLecture.getId());
        dto.setPosition(hoursLecture.getPosition());
        return dto;
    }

    private HoursLecture mapDtoToEntity(HoursLectureDto dto){
        HoursLecture hoursLecture = new HoursLecture();
        hoursLecture.setStartLectureTime(dto.getStartLectureTime());
        hoursLecture.setId(dto.getId());
        hoursLecture.setPosition(dto.getPosition());
        return hoursLecture;
    }
}
