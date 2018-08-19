package pl.timetable.service;

import pl.timetable.dto.Genotype;
import pl.timetable.dto.LectureDescriptionDto;

public interface AbstractCriteria {

    public boolean checkData(Genotype genotype, LectureDescriptionDto lectureDescriptionDto);
}
