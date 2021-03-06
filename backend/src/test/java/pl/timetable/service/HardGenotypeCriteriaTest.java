package pl.timetable.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import pl.timetable.dto.Cell;
import pl.timetable.dto.GroupDto;
import pl.timetable.dto.RoomDto;

import java.util.*;

import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class HardGenotypeCriteriaTest {

    @InjectMocks
    private HardGenotypeCriteria hardGenotypeCriteria;

    @Test
    public void checkEmptyLectureByGroupSuccessTest(){
//        Map<GroupDto, List<RoomDto>> roomByGroup = new HashMap<>();
//        RoomDto[] roomDtos = new RoomDto[]{null, new RoomDto(), new RoomDto(), new RoomDto(), null, null};
////        roomByGroup.putIfAbsent(new GroupDto(), Arrays.asList(roomDtos));
//        boolean isValid = hardGenotypeCriteria.hasNoEmptyLectureByGroup(Arrays.asList(roomDtos), 5);
//        Assert.assertTrue(isValid);
    }

    @Test
    public void checkEmptyLectureByGroupFailureTest(){
//        Map<GroupDto, List<RoomDto>> roomByGroup = new HashMap<>();
//        RoomDto[] roomDtos = new RoomDto[]{null, new RoomDto(), null, new RoomDto(), null, null};
////        roomByGroup.putIfAbsent(new GroupDto(), Arrays.asList(roomDtos));
//        boolean isValid = hardGenotypeCriteria.hasNoEmptyLectureByGroup(Arrays.asList(roomDtos),5);
//        Assert.assertFalse(isValid);
    }

    @Test
    public void checkSizeLectureToSubjectSuccessTest(){
        boolean isValid = hardGenotypeCriteria.hasEnoughSizeLectureToSubject(10, 10);
        Assert.assertTrue(isValid);
    }

    @Test
    public void checkSizeLectureToSubjectFailureTest(){
        boolean isValid = hardGenotypeCriteria.hasEnoughSizeLectureToSubject(10, 110);
        Assert.assertFalse(isValid);
    }

    @Test
    public void hasNoGroupDuplicatesByRoom(){
        Cell[][] genotypesTable = new Cell[2][4];
        for (int i = 0; i < genotypesTable.length ; i++) {
            for (int j = 0; j < genotypesTable[i].length; j++) {
                genotypesTable[i][j] = new Cell(j, null, null, null);
                genotypesTable[i][j].setRoomDto(new RoomDto("", "", i+j));
            }
        }
        boolean isValid = hardGenotypeCriteria.hasNoGroupDuplicatesByRoom(genotypesTable);
        Assert.assertTrue(isValid);
    }

    @Test
    public void hasGroupDuplicatesByRoom(){
        Cell[][] genotypesTable = new Cell[2][4];
        for (int i = 0; i < genotypesTable.length ; i++) {
            for (int j = 0; j < genotypesTable[i].length; j++) {
                genotypesTable[i][j] = new Cell(j, null, null, null);
                genotypesTable[i][j].setRoomDto(new RoomDto("", "", j));
            }
        }
        boolean isValid = hardGenotypeCriteria.hasNoGroupDuplicatesByRoom(genotypesTable);
        Assert.assertFalse(isValid);
    }

    @Test
    public void hasNoTeacherPerLectureDuplicatesTestSuccess(){
        Map<Integer, List<Integer>> lectureByTeacher = new HashMap<>();
        List<Integer> lectures = new ArrayList<>();
        lectures.add(1);
        lectures.add(2);
        lectures.add(3);
        lectureByTeacher.putIfAbsent(1,lectures);
        boolean isValid = hardGenotypeCriteria.hasNoTeacherPerLectureDuplicates(lectureByTeacher);
        Assert.assertThat(isValid, is(true));
    }

    @Test
    public void hasNoTeacherPerLectureDuplicatesTestFailure(){
        Map<Integer, List<Integer>> lectureByTeacher = new HashMap<>();
        List<Integer> lectures = new ArrayList<>();
        lectures.add(1);
        lectures.add(2);
        lectures.add(1);
        lectureByTeacher.putIfAbsent(1,lectures);
        boolean isValid = hardGenotypeCriteria.hasNoTeacherPerLectureDuplicates(lectureByTeacher);
        Assert.assertThat(isValid, is(false));
    }
}
