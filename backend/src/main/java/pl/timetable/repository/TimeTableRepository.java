package pl.timetable.repository;

import pl.timetable.entity.TimeTable;

import java.util.List;

public interface TimeTableRepository extends GenericRepository<TimeTable>{

    public List<TimeTable> getTimeTableRowsByDays(List<Integer> days);
    public List<TimeTable> getTimeTableResult(Integer timeTableId, Integer firstResult, Integer maxResult, Integer groupId);
    List<TimeTable> getTimeTableResult(Integer timeTableId);
    public Integer getTimeTableCount(Integer timeTableId, Integer groupId);
}
