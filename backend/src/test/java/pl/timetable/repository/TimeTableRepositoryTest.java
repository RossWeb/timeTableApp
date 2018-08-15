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
import pl.timetable.entity.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TimeTableRepositoryTest {

    @TestConfiguration
    static class TimeTableRepositoryTestContextConfiguration {
        @Bean
        public RoomRepository roomRepository() {return new RoomRepositoryImpl();};
        @Bean
        public TimeTableRepository timeTableRepository() {return new TimeTableRepositoryImpl();};
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
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TimeTableRepository timeTableRepository;

    @Test
    public void createTimeTableTest() {
        TimeTable timeTable = new TimeTable();
        timeTable.setDay(1);
        timeTable.setLectureNumber(1);
        timeTable.setRoom(getRoomEntity());
        Group group = getGroupEntity();
        timeTable.setGroup(getGroupEntity());
        timeTable.setSubject((Subject)group.getCourse().getSubjectSet().toArray()[0]);
        TimeTable timeTableCreated = timeTableRepository.create(timeTable);
        Assert.assertNotNull(timeTableCreated.getId());
        Assert.assertNotNull(timeTableCreated.getGroup());
        Assert.assertNotNull(timeTableCreated.getSubject());
        Assert.assertNotNull(timeTableCreated.getRoom());
    }

    @Test
    public void findTimeTableTest() {
        TimeTable timeTableCreated = createTimeTableRow();
        TimeTable timeTableFounded = timeTableRepository.getById(timeTableCreated.getId());
        Assert.assertNotNull(timeTableFounded.getId());
        Assert.assertEquals(timeTableCreated.getId(),timeTableFounded.getId());

    }

    @Test
    public void findByWeeks(){
        createTimeTableRow();
        createTimeTableRow();
        List<Integer> days = new ArrayList<>();
        days.add(1);
        List<TimeTable> timeTables = timeTableRepository.getTimeTableRowsByDays(days);
        Assert.assertThat(timeTables.isEmpty(), is(false));
        Assert.assertThat(timeTables.size(), is(2));

    }

    private TimeTable createTimeTableRow(){
        TimeTable timeTable = new TimeTable();
        timeTable.setLectureNumber(1);
        timeTable.setDay(1);
        timeTable.setLectureNumber(1);
        timeTable.setRoom(getRoomEntity());
        Group group = getGroupEntity();
        timeTable.setGroup(getGroupEntity());
        timeTable.setSubject((Subject)group.getCourse().getSubjectSet().toArray()[0]);
        return timeTableRepository.create(timeTable);

    }


    private Room getRoomEntity(){
        Room room = new Room();
        room.setNumber("1");
        room.setName("Nazwa");
        return roomRepository.create(room);
    }
    private Group getGroupEntity(){
        Subject subject = new Subject();
        subject.setName("Matematyka");
        Subject subjectCreated = subjectRepository.create(subject);
        Course course = new Course();
        course.setName("Informatyka");
        Set<Subject> subjects = new HashSet<>();
        subjects.add(subjectCreated);
        course.setSubjectSet(subjects);
        Course courseCreated = courseRepository.create(course);
        Group group = new Group();
        group.setName("14A");
        group.setCourse(courseCreated);
        return groupRepository.create(group);
    }
}
