package pl.timetable.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.dto.LectureDescriptionDto;

@RunWith(MockitoJUnitRunner.class)
public class InitialGenotypeCriteriaTest {

    @InjectMocks
    private InitialGenotypeCriteria initialGenotypeCriteria;

    @Test
    public void roomAndLectureToSubjectIsValid(){
        LectureDescriptionDto lectureDescriptionDto = new LectureDescriptionDto(4,2, 8);
        GeneticInitialData geneticInitialData = GeneticUtilityTestClass.getGeneticInitialDataParametrized(lectureDescriptionDto, 5, 5, 3, 5, 4);
        boolean isValid = initialGenotypeCriteria.checkData(geneticInitialData);
        Assert.assertTrue(isValid);
    }

    @Test
    public void roomAndLectureToSubjectIsNotValid(){
        LectureDescriptionDto lectureDescriptionDto = new LectureDescriptionDto(4,2, 1);
        GeneticInitialData geneticInitialData =
                GeneticUtilityTestClass.getGeneticInitialDataParametrized(lectureDescriptionDto, 5, 5, 3, 5, 4);
        boolean isValid = initialGenotypeCriteria.checkData(geneticInitialData);
        Assert.assertTrue(isValid);
    }
}
