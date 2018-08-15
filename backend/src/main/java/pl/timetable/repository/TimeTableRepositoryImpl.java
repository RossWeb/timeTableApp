package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.TimeTable;

import java.util.List;

@Repository
public class TimeTableRepositoryImpl extends AbstractGenericRepositoryWithSession<TimeTable> implements TimeTableRepository {

    public List<TimeTable> getTimeTableRowsByDays(List<Integer> days){
        return getSession().getNamedQuery("findTimeTableByDays").setParameterList("days", days).list();
    }
}
