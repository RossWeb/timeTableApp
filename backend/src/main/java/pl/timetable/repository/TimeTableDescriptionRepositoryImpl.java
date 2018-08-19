package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.TimeTableDescription;

@Repository
public class TimeTableDescriptionRepositoryImpl extends AbstractGenericRepositoryWithSession<TimeTableDescription> implements TimeTableDescriptionRepository {
}
