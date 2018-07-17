package pl.timetable.service;

import pl.timetable.dto.Genotype;

public interface AbstractCriteria {

    public boolean checkData(Genotype genotype);
}
