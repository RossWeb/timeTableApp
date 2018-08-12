package pl.timetable.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import pl.timetable.dto.Cell;
import pl.timetable.dto.GroupDto;
import pl.timetable.dto.RoomDto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class HardGenotypeCriteriaTest {

    @InjectMocks
    private HardGenotypeCriteria hardGenotypeCriteria;

    @Test
    public void checkEmptyLectureByGroupSuccessTest(){
        Map<GroupDto, List<RoomDto>> roomByGroup = new HashMap<>();
        RoomDto[] roomDtos = new RoomDto[]{null, new RoomDto(), new RoomDto(), new RoomDto(), null, null};
//        roomByGroup.putIfAbsent(new GroupDto(), Arrays.asList(roomDtos));
        boolean isValid = hardGenotypeCriteria.hasNoEmptyLectureByGroup(Arrays.asList(roomDtos), 5);
        Assert.assertTrue(isValid);
    }

    @Test
    public void checkEmptyLectureByGroupFailureTest(){
        Map<GroupDto, List<RoomDto>> roomByGroup = new HashMap<>();
        RoomDto[] roomDtos = new RoomDto[]{null, new RoomDto(), null, new RoomDto(), null, null};
//        roomByGroup.putIfAbsent(new GroupDto(), Arrays.asList(roomDtos));
        boolean isValid = hardGenotypeCriteria.hasNoEmptyLectureByGroup(Arrays.asList(roomDtos),5);
        Assert.assertFalse(isValid);
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
                genotypesTable[i][j] = new Cell(j, new RoomDto("", "", i+j), null, null, null);
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
                genotypesTable[i][j] = new Cell(j, new RoomDto("", "", j), null, null, null);
            }
        }
        boolean isValid = hardGenotypeCriteria.hasNoGroupDuplicatesByRoom(genotypesTable);
        Assert.assertFalse(isValid);
    }
}
