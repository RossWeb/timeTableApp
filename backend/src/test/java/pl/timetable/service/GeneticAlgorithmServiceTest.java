package pl.timetable.service;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.dto.Population;
import pl.timetable.facade.TimeTableFacade;
import pl.timetable.repository.CourseRepository;
import pl.timetable.repository.CourseRepositoryImpl;
import pl.timetable.repository.GroupRepository;
import pl.timetable.repository.GroupRepositoryImpl;

import java.util.Arrays;
import java.util.Objects;

import static pl.timetable.service.GeneticUtilityTestClass.getGeneticInitialData;

@RunWith(MockitoJUnitRunner.class)
public class GeneticAlgorithmServiceTest {

    @InjectMocks
    private GeneticAlgorithmService geneticAlgorithmService;

    @Mock
    private TimeTableFacade timeTableFacade;

    @Before
    public void init(){
        HardGenotypeCriteria hardGenotypeCriteria = new HardGenotypeCriteria();
        SoftGenotypeCriteria softGenotypeCriteria = new SoftGenotypeCriteria();
        GenotypeService genotypeService = new GenotypeService(hardGenotypeCriteria);
        genotypeService.setHardGenotypeCriteria(hardGenotypeCriteria);
        FitnessService fitnessService = new FitnessService(hardGenotypeCriteria,genotypeService, softGenotypeCriteria);
        geneticAlgorithmService.setGenotypeService(genotypeService);
        geneticAlgorithmService.setFitnessService(fitnessService);
        geneticAlgorithmService.setTimeTableFacade(timeTableFacade);
    }

    @Test
    //uwzglednic async
    public void initTest(){
        //given
        //when
        GeneticInitialData geneticInitialData = getGeneticInitialData();
        geneticInitialData.setPopulationSize(100);
        Integer timeTableId = geneticAlgorithmService.init(geneticInitialData);
        //then
        Assert.assertNotNull(timeTableId);
//        Assert.assertNotNull(population);
//        Assert.assertEquals(100, population.getGenotypePopulation().size());
//        Assert.assertFalse(Arrays.stream(population.getSelectionArray()).anyMatch(Objects::isNull));
    }

}
