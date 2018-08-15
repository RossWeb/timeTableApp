package pl.timetable.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.dto.Population;

import java.util.Arrays;
import java.util.Objects;

import static pl.timetable.service.GeneticUtilityTestClass.getGeneticInitialData;

@RunWith(MockitoJUnitRunner.class)
public class GeneticAlgorithmServiceTest {

    @InjectMocks
    private GeneticAlgorithmService geneticAlgorithmService;

    @Before
    public void init(){
        HardGenotypeCriteria hardGenotypeCriteria = new HardGenotypeCriteria();
        SoftGenotypeCriteria softGenotypeCriteria = new SoftGenotypeCriteria();
        GenotypeService genotypeService = new GenotypeService(hardGenotypeCriteria);
        genotypeService.setHardGenotypeCriteria(hardGenotypeCriteria);
        FitnessService fitnessService = new FitnessService(hardGenotypeCriteria, softGenotypeCriteria);
        geneticAlgorithmService.setGenotypeService(genotypeService);
        geneticAlgorithmService.setFitnessService(fitnessService);
    }

    @Test
    public void initTest(){
        //given
        //when
        GeneticInitialData geneticInitialData = getGeneticInitialData();
        geneticInitialData.setPopulationSize(100);
        Population population = geneticAlgorithmService.init(geneticInitialData);
        //then
        Assert.assertNotNull(population);
        Assert.assertEquals(100, population.getGenotypePopulation().size());
        Assert.assertFalse(Arrays.stream(population.getSelectionArray()).anyMatch(Objects::isNull));
    }

}
