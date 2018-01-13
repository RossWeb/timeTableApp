package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.timetable.api.CourseRequest;
import pl.timetable.entity.Course;
import pl.timetable.entity.Subject;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.CourseRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseServiceImpl extends AbstractService<Course, CourseRequest> {

    public static final Logger LOGGER = Logger.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectServiceImpl subjectService;

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll().orElse(Collections.emptyList());
    }

    @Override
    public void create(CourseRequest request) throws EntityNotFoundException {
        Course course = new Course();
        course.setName(request.getName());
        if(Objects.nonNull(request.getSubject())) {
            Subject subject = subjectService.get(request.getSubject());
            course.getSubjectSet().add(subject);
        }
        courseRepository.create(course);
    }

    @Override
    public Course update(CourseRequest request, int id) throws EntityNotFoundException {
        Subject subject = subjectService.get(request.getSubject());
        Course course = new Course();
        course.setName(request.getName());
        course.getSubjectSet().add(subject);
        course.setId(id);
        return courseRepository.update(course);
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        Course course = get(id);
        courseRepository.remove(course);
    }

    @Override
    public Course get(int id) throws EntityNotFoundException {
        return Optional.ofNullable(courseRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Course"));
    }
}
