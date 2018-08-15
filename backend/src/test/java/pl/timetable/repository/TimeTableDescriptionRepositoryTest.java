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
import pl.timetable.entity.TimeTableDescription;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TimeTableDescriptionRepositoryTest {

    @TestConfiguration
    static class TimeTableRepositoryTestContextConfiguration {
        @Bean
        public TimeTableDescriptionRepository timeTableDescriptionRepository() {return new TimeTableDescriptionRepositoryImpl();};
    }

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TimeTableDescriptionRepository timeTableDescriptionRepository;

    @Test
    public void createTimeTableDescriptionTest(){
        TimeTableDescription timeTableDescription = new TimeTableDescription();
        timeTableDescription.setName("TestName");
        timeTableDescription.setCreatedDate(LocalDateTime.now());
        TimeTableDescription tableDescriptionCreated = timeTableDescriptionRepository.create(timeTableDescription);
        Assert.assertThat(tableDescriptionCreated.getId(), notNullValue());
    }

    @Test
    public void findTimeTableById(){
        TimeTableDescription timeTableDescription = new TimeTableDescription();
        timeTableDescription.setName("TestName");
        timeTableDescription.setCreatedDate(LocalDateTime.now());
        timeTableDescriptionRepository.create(timeTableDescription);
        TimeTableDescription tableDescriptionFound = timeTableDescriptionRepository.getById(1);
        Assert.assertThat(tableDescriptionFound, notNullValue());
        Assert.assertThat(tableDescriptionFound.getName(), is("TestName"));
    }

}

