package pl.timetable.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import pl.timetable.entity.Room;
import pl.timetable.repository.RoomRepository;
import pl.timetable.repository.RoomRepositoryImpl;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoomRepositoryTest {

    @TestConfiguration
    static class RoomRepositoryTestContextConfiguration {

        @Bean
        public RoomRepository roomRepository() {
            return new RoomRepositoryImpl();
        }
    }

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void createRoomTest() {
        Room room = new Room();
        room.setName("someName");
        room.setNumber("1");
        Room roomCreated = roomRepository.create(room);
        Assert.assertEquals(1, roomCreated.getId());

    }

    @Test
    public void findRoomTest() {
        Room room = new Room();
        room.setName("someName");
        room.setNumber("1");
        roomRepository.create(room);
        Room roomCreated = roomRepository.getById(1);
        Assert.assertEquals(1, roomCreated.getId());

    }

    @Test
    public void findAllRoomTest() {
        Room room = new Room();
        room.setName("someName");
        room.setNumber("1");
        roomRepository.create(room);
        List<Room> roomList = roomRepository.findAll().get();
        Assert.assertNotNull(roomList);
        Assert.assertTrue(!roomList.isEmpty());

    }

}
