package pl.timetable.service;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.CourseRequest;
import pl.timetable.dto.CourseDto;
import pl.timetable.entity.Course;
import pl.timetable.entity.Subject;
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
public class CourseServiceImpl extends AbstractService<CourseDto, CourseRequest> {

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
            return mapEntityToDto(course);}).collect(Collectors.toList());
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

    @Override
    public CourseDto get(int id) throws EntityNotFoundException {
        Course course = Optional.ofNullable(courseRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Course"));
        return mapEntityToDto(course);
    }

    private Subject getSubject(Integer subjectId) throws EntityNotFoundException {
        return Optional.ofNullable(subjectRepository.getById(subjectId))
                .orElseThrow(() -> new EntityNotFoundException(subjectId, "Subject"));
    }

    private CourseDto mapEntityToDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setSubjectSet(course.getSubjectSet());
        return courseDto;
    }
}
