package pl.timetable.repository;

import pl.timetable.entity.TimeTable;

import java.util.List;

public interface TimeTableRepository extends GenericRepository<TimeTable>{

    public List<TimeTable> getTimeTableRowsByDays(List<Integer> days);
}
