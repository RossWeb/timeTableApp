package pl.timetable.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.timetable.entity.Course;
import pl.timetable.entity.Group;
import pl.timetable.entity.Room;
import pl.timetable.entity.Subject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class GroupRepositoryTest {

    @TestConfiguration
    static class GroupRepositoryTestContextConfiguration {

        @Bean
        public GroupRepository groupRepository() {
            return new GroupRepositoryImpl();
        }
        @Bean
        public SubjectRepository subjectRepository() {return new SubjectRepositoryImpl();}
        @Bean
        public CourseRepository courseRepository() {
            return new CourseRepositoryImpl();
        }
    }

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    public void createGroupTest() {
        Group group = new Group();
        group.setName("14A");
        group.setCourse(getCourseEntity());
        Group groupCreated = groupRepository.create(group);
        Assert.assertNotNull(groupCreated.getId());
        Assert.assertNotNull(groupCreated.getCourse());
    }

    @Test
    public void findGroupTest() {
        Group group = new Group();
        group.setName("14A");
        group.setCourse(getCourseEntity());
        Group groupCreated = groupRepository.create(group);
        Group groupFounded = groupRepository.getById(groupCreated.getId());
        Assert.assertNotNull(groupFounded.getId());
        Assert.assertEquals(groupCreated.getId(),groupFounded.getId());

    }

    private Course getCourseEntity(){
        Subject subject = new Subject();
        subject.setName("Matematyka");
        Subject subjectCreated = subjectRepository.create(subject);
        Course course = new Course();
        course.setName("Informatyka");
        Set<Subject> subjects = new HashSet<>();
        subjects.add(subjectCreated);
        course.setSubjectSet(subjects);
        Course courseCreated = courseRepository.create(course);
        return courseCreated;
    }
}
