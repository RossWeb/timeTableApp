package pl.timetable.service;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import pl.timetable.dto.Cell;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.dto.Genotype;
import pl.timetable.dto.RoomDto;

import java.util.*;

import static pl.timetable.service.GeneticUtilityTestClass.getGeneticInitialData;

@RunWith(MockitoJUnitRunner.class)
public class GenotypeServiceTest {

    private static final Logger LOGGER = Logger.getLogger(GenotypeServiceTest.class);



    @InjectMocks
    private GenotypeService genotypeService;

    @Before
    public void init(){
        genotypeService.setHardGenotypeCriteria(new HardGenotypeCriteria());
    }

    @Test
    public void validateGenotype(){
        //given
        //when
        Genotype genotype;
        for (;  ; ) {
            genotype = genotypeService.createInitialGenotype(getGeneticInitialData());

        }
        //then
    }

    @Test
    public void createInitialGenotypeTest() {
        //given
        final GeneticInitialData geneticInitialData = getGeneticInitialData();
        final Integer lectureSize = geneticInitialData.getLecture().getNumberPerDay() * geneticInitialData.getLecture().getDaysPerWeek();
        final Integer lectureAndRoomSize = lectureSize * geneticInitialData.getRoomDtoList().size();
        //when
        Genotype genotype = genotypeService.createInitialGenotype(geneticInitialData);
        //then
        Cell[] genotypeElement = genotype.getGenotypeTable()[0];

        Assert.assertEquals("lectureNumber must be correct for genotype", lectureSize, genotypeElement[genotypeElement.length-1].getLecture());
        Assert.assertEquals("cell number must be correct" , lectureAndRoomSize, (Integer) genotypeElement.length);

        Set<Integer> lectureKindSet = new HashSet<>();
        List<RoomDto> roomPerLectureListArray = new ArrayList<>();
        for (Cell aGenotypeElement : genotypeElement) {
            lectureKindSet.add(aGenotypeElement.getLecture());
            if (Objects.nonNull(aGenotypeElement.getRoomDto())) {
                roomPerLectureListArray.add(aGenotypeElement.getRoomDto());
            }
        }
        Integer roomSizeByLecture = genotype.getRoomByLecture().values().stream().map(List::size).mapToInt(value -> value).sum();

        Assert.assertEquals("lecture number kind must be correct" , lectureSize, (Integer) lectureKindSet.size());
        Assert.assertTrue("room per lecture must be one for array", lectureSize >= (Integer) roomPerLectureListArray.size());
        Assert.assertTrue("room per lecture must be one for map", (Integer)(lectureSize*geneticInitialData.getGroupDtoList().size()) >= roomSizeByLecture);

    }




}
