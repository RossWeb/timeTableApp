package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.TimeTable;

@Repository
public class TimeTableRepositoryImpl extends AbstractGenericRepositoryWithSession<TimeTable> implements TimeTableRepository {
}
