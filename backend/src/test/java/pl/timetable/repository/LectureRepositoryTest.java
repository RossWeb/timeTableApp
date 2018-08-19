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
import pl.timetable.entity.LectureDescription;
import pl.timetable.entity.TimeTableDescription;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LectureRepositoryTest {

    @TestConfiguration
    static class LectureRepositoryTestContextConfiguration {
        @Bean
        public LectureDescriptionRepository lectureDescriptionRepository() {return new LectureDescriptionRepositoryImpl();};
        @Bean
        public TimeTableDescriptionRepository timeTableDescriptionRepository() {return new TimeTableDescriptionRepositoryImpl();};
    }

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private LectureDescriptionRepository lectureDescriptionRepository;
    @Autowired
    private TimeTableDescriptionRepository timeTableDescriptionRepository;


    @Test
    public void createLectureTest(){
        LectureDescription lectureDescriptionCreated = createLectureDescription();
        Assert.assertThat(lectureDescriptionCreated.getId(), notNullValue());
    }

    private LectureDescription createLectureDescription() {
        TimeTableDescription timeTableDescription = new TimeTableDescription();
        timeTableDescription.setName("TestName");
        timeTableDescription.setCreatedDate(LocalDateTime.now());
        timeTableDescription = timeTableDescriptionRepository.create(timeTableDescription);
        LectureDescription lectureDescription = new LectureDescription();
        lectureDescription.setDaysPerWeek(5);
        lectureDescription.setNumberPerDay(5);
        lectureDescription.setWeeksPerSemester(2);
        lectureDescription.setTimeTableDescription(timeTableDescription);
        return lectureDescriptionRepository.create(lectureDescription);
    }

    @Test
    public void findDaysPerWeekByTimeTableId(){
        LectureDescription lectureDescription = createLectureDescription();
        Integer daysPerWeek = lectureDescriptionRepository.getByTimeTableDescription(lectureDescription.getTimeTableDescription()).getDaysPerWeek();
        Assert.assertThat(daysPerWeek, notNullValue());
        Assert.assertThat(daysPerWeek, is(5));
    }

}
