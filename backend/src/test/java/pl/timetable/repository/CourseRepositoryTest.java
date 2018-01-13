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
import pl.timetable.entity.Subject;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CourseRepositoryTest {

    @TestConfiguration
    static class CourseRepositoryTestContextConfiguration {

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
    private CourseRepository courseRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    public void createTest(){
        //given
        Subject subject = new Subject();
        subject.setName("Matematyka");
        Subject subjectCreated = subjectRepository.create(subject);
        Course course = new Course();
        course.setName("Informatyka");
        Set<Subject> subjects = new HashSet<>();
        subjects.add(subjectCreated);
        course.setSubjectSet(subjects);
        //when
        Course courseCreated = courseRepository.create(course);
        //then
        Assert.assertNotNull(courseCreated);
        Assert.assertNotNull(courseCreated.getSubjectSet());
        Assert.assertNotNull(courseCreated.getId());
        Assert.assertTrue(courseCreated.getSubjectSet().size() == 1);
    }

    public void findTest(){
        //given
        Subject subject = new Subject();
        subject.setName("Matematyka");
        Subject subjectCreated = subjectRepository.create(subject);
        Course course = new Course();
        course.setName("Informatyka");
        Set<Subject> subjects = new HashSet<>();
        subjects.add(subjectCreated);
        course.setSubjectSet(subjects);
        Course courseCreated = courseRepository.create(course);
        //when
        Course courseFounded = courseRepository.getById(courseCreated.getId());
        //then
        Assert.assertNotNull(courseCreated);
        Assert.assertNotNull(courseCreated.getSubjectSet());
        Assert.assertNotNull(courseCreated.getId());
        Assert.assertTrue(courseCreated.getSubjectSet().size() == 1);
    }
}
