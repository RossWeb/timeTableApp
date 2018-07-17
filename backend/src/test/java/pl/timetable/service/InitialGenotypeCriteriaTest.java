package pl.timetable.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.dto.Lecture;

@RunWith(MockitoJUnitRunner.class)
public class InitialGenotypeCriteriaTest {

    @InjectMocks
    private InitialGenotypeCriteria initialGenotypeCriteria;

    @Test
    public void roomAndLectureToSubjectIsValid(){
        Lecture lecture = new Lecture(4,2, 8);
        GeneticInitialData geneticInitialData = GeneticUtilityTestClass.getGeneticInitialDataParametrized(lecture, 5, 5, 3, 5);
        boolean isValid = initialGenotypeCriteria.checkData(geneticInitialData);
        Assert.assertTrue(isValid);
    }

    @Test
    public void roomAndLectureToSubjectIsNotValid(){
        Lecture lecture = new Lecture(4,2, 1);
        GeneticInitialData geneticInitialData =
                GeneticUtilityTestClass.getGeneticInitialDataParametrized(lecture, 5, 5, 3, 5);
        boolean isValid = initialGenotypeCriteria.checkData(geneticInitialData);
        Assert.assertTrue(isValid);
    }
}
