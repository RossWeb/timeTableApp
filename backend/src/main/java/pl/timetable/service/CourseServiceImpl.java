package pl.timetable.service;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.CourseRequest;
import pl.timetable.api.CourseResponse;
import pl.timetable.dto.CourseDto;
import pl.timetable.entity.Course;
import pl.timetable.entity.Subject;
import pl.timetable.exception.EntityDuplicateFoundException;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.CourseRepository;
import pl.timetable.repository.SubjectRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl extends AbstractService<CourseDto, CourseRequest, CourseResponse> {

    public static final Logger LOGGER = Logger.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<CourseDto> findAll() {
        List<Course> entityCourses = courseRepository.findAll().orElse(Collections.emptyList());
        return entityCourses.stream().map(course -> {
            Hibernate.initialize(course.getSubjectSet());
            return mapEntityToDto(course);
        }).collect(Collectors.toList());
    }

    @Override
    public CourseResponse find(CourseRequest request) {
        CourseResponse courseResponse = new CourseResponse();
        Integer first = request.getPageNumber() * request.getSize();
        Integer max = first + request.getSize();
        List<Course> courseList = courseRepository.getResult(first, max).orElse(Collections.emptyList());
        courseResponse.setData(courseList.stream().map(course -> {
            Hibernate.initialize(course.getSubjectSet());
            return mapEntityToDto(course);
        }).collect(Collectors.toList()));
        courseResponse.setTotalElements(findAll().size());
        return courseResponse;
    }

    @Override
    public void create(CourseRequest request) throws EntityNotFoundException {
        Course course = new Course();
        course.setName(request.getName());
        if (Objects.nonNull(request.getSubject())) {
            course.getSubjectSet().add(getSubject(request.getSubject()));
        }
        courseRepository.create(course);
    }

    public Course createAndAddSubjectIfNeeded(CourseDto courseDto, Subject subject){
        Course course = mapDtoToEntity(courseDto);
        return Optional.ofNullable(courseRepository.getCourseByName(course.getName())).map(courseFound -> {
            Subject subjectFound = subjectRepository.getById(subject.getId());
            if(!courseFound.getSubjectSet().contains(subjectFound)){
                course.getSubjectSet().add(subjectFound);
                return courseRepository.update(course);
            }else{
                return course;
            }

        }).orElseGet(()-> courseRepository.create(course));
    }

    @Override
    public CourseDto update(CourseRequest request, int id) throws EntityNotFoundException {
        Course course = new Course();
        course.setName(request.getName());
        course.getSubjectSet().add(getSubject(request.getSubject()));
        course.setId(id);
        return mapEntityToDto(courseRepository.update(course));
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        Course course = Optional.ofNullable(courseRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Course"));
        courseRepository.remove(course);
    }

    public void deleteParameter(int id, int parameterId) throws EntityNotFoundException {
        Course course = Optional.ofNullable(courseRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Course"));
        Subject subjectFounded = course.getSubjectSet().stream().filter(subject -> parameterId == subject.getId()).findAny()
                .orElseThrow(() -> new EntityNotFoundException(parameterId, "Subject"));
        course.getSubjectSet().remove(subjectFounded);
        courseRepository.update(course);
    }

    public void addParameter(int id, int parameterId) throws EntityNotFoundException, EntityDuplicateFoundException {
        Course course = Optional.ofNullable(courseRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Course"));
        course.getSubjectSet().stream()
            .filter(subject -> subject.getId() == parameterId).findAny().ifPresent(
                    subject -> new EntityDuplicateFoundException(parameterId, "Subject")
            );
        Subject subjectToAdd = subjectRepository.getById(parameterId);
        course.getSubjectSet().add(subjectToAdd);
        courseRepository.update(course);

    }

    @Override
    public CourseDto get(int id) throws EntityNotFoundException {
        Course course = Optional.ofNullable(courseRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Course"));
        Hibernate.initialize(course.getSubjectSet());
        return mapEntityToDto(course);
    }

    public List<Subject> getParameters(int id) throws EntityNotFoundException {
        return Optional.ofNullable(courseRepository.getParameters(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "SubjectList"));
    }

    private Subject getSubject(Integer subjectId) throws EntityNotFoundException {
        return Optional.ofNullable(subjectRepository.getById(subjectId))
                .orElseThrow(() -> new EntityNotFoundException(subjectId, "Subject"));
    }

    public static CourseDto mapEntityToDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setSubjectSet(course.getSubjectSet());
        return courseDto;
    }

    private Course mapDtoToEntity(CourseDto courseDto){
        Course course = new Course();
        course.setName(courseDto.getName());
        course.setSubjectSet(courseDto.getSubjectSet());
        return course;
    }
}
