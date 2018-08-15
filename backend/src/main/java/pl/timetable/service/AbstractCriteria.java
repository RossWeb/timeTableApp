package pl.timetable.service;

import pl.timetable.dto.Genotype;
import pl.timetable.dto.LectureDescription;

public interface AbstractCriteria {

    public boolean checkData(Genotype genotype, LectureDescription lectureDescription);
}
