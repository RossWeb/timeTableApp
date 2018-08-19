package pl.timetable.repository;

import pl.timetable.entity.ReportPopulation;

import java.util.List;

public interface ReportPopulationRepository extends GenericRepository<ReportPopulation> {
    public List<ReportPopulation> getByTimeTableDescription(Integer timeTableDescriptionId);
}
