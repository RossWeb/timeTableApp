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
import pl.timetable.entity.Subject;

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
        final int lectureSize = geneticInitialData.getLectureDescription().getNumberPerDay() * geneticInitialData.getLectureDescription().getWeeksPerSemester()* geneticInitialData.getLectureDescription().getDaysPerWeek();
        final int subjectSize = geneticInitialData.getGroupDtoList().get(0).getCourse().getSubjectSet().stream().map(Subject::getSize).mapToInt(value -> value).sum();
        //when
        Genotype genotype = genotypeService.createInitialGenotype(geneticInitialData);
        //then
        Arrays.stream(genotype.getGenotypeTable()).forEach(group -> {
            Cell[] genotypeElement = group;

            Assert.assertEquals("lectureNumber must be correct for genotype", (lectureSize), genotypeElement.length);
            Assert.assertEquals("subjectSize must be equals room " , subjectSize  , ((Long)Arrays.stream(genotypeElement).filter(cell -> Objects.nonNull(cell) && Objects.nonNull(cell.getRoomDto())).count()).intValue());

            Set<Integer> lectureKindSet = new HashSet<>();
            List<RoomDto> roomPerLectureListArray = new ArrayList<>();
            for (Cell aGenotypeElement : genotypeElement) {
                if(Objects.nonNull(aGenotypeElement) && Objects.nonNull(aGenotypeElement.getRoomDto())) {
                    lectureKindSet.add(aGenotypeElement.getLecture());
                    roomPerLectureListArray.add(aGenotypeElement.getRoomDto());
                }
            }
            Integer roomSizeByLecture = genotype.getRoomByLecture().values().stream().map(List::size).mapToInt(value -> value).sum();

            Assert.assertEquals("lecture number kind must be correct" , subjectSize, lectureKindSet.size());
            Assert.assertTrue("room per lecture must be one for array", lectureSize >= (Integer) roomPerLectureListArray.size());
            Assert.assertTrue("room per lecture must be one for map", (Integer)(lectureSize*geneticInitialData.getGroupDtoList().size()) >= roomSizeByLecture);
        });
        Assert.assertEquals("all subjects has room", subjectSize*geneticInitialData.getGroupDtoList().size() , genotype.getRoomBySubject().values().stream().mapToInt(List::size).sum());


    }




}
