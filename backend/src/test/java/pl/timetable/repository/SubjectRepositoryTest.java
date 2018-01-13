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
import pl.timetable.entity.Room;
import pl.timetable.entity.Subject;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubjectRepositoryTest {

    @TestConfiguration
    static class SubjectRepositoryTestContextConfiguration {

        @Bean
        public SubjectRepository subjectRepository() {
            return new SubjectRepositoryImpl();
        }
    }

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    public void createSubjectTest() {
        Subject subject = new Subject();
        subject.setName("Matematyka");
        Subject subjectCreated = subjectRepository.create(subject);
        Assert.assertEquals("Matematyka", subjectCreated.getName());

    }

    @Test
    public void findRoomTest() {
        Subject subject = new Subject();
        subject.setName("Matematyka");
        Subject subjectCreated = subjectRepository.create(subject);
        Subject subjectFounded = subjectRepository.getById(subjectCreated.getId());
        Assert.assertEquals(subjectCreated.getId(), subjectFounded.getId());

    }

    @Test
    public void findAllRoomTest() {
        Subject subject = new Subject();
        subject.setName("Matematyka");
        subjectRepository.create(subject);
        List<Subject> subjectList = subjectRepository.findAll().get();
        Assert.assertNotNull(subjectList);
        Assert.assertTrue(!subjectList.isEmpty());

    }

}
