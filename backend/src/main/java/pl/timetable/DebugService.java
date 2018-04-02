package pl.timetable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.entity.Course;
import pl.timetable.entity.Group;
import pl.timetable.entity.Subject;
import pl.timetable.repository.CourseRepository;
import pl.timetable.repository.GroupRepository;
import pl.timetable.repository.SubjectRepository;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class DebugService {

    private static Log Logger = LogFactory.getLog(MyApplication.class);

    private final CourseRepository courseRepository;

    private final SubjectRepository subjectRepository;

    private final GroupRepository groupRepository;

    @Autowired
    public DebugService(CourseRepository courseRepository, SubjectRepository subjectRepository, GroupRepository groupRepository) {
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
    }

    public void init() {
            Logger.info("Fill datatable default value");
            Subject subject = createSubject();
            Course course = createCourse(subject);
            createGroup(course);
    }

    private void createGroup(Course course) {
        Group group = new Group();
        group.setCourse(course);
        group.setName("Informatyka 1");
        groupRepository.create(group);
    }

    private Course createCourse(Subject subject) {
        Set<Subject> subjectSet = new HashSet<>();
        subjectSet.add(subject);
        Course course = new Course();
        course.setName("Informatyka");
        course.setSubjectSet(subjectSet);
        return courseRepository.create(course);
    }

    private Subject createSubject() {
        Subject subject = new Subject();
        subject.setName("Matematyka");
        return subjectRepository.create(subject);
    }
}
