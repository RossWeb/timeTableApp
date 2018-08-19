package pl.timetable.repository;

import pl.timetable.entity.LectureDescription;
import pl.timetable.entity.TimeTableDescription;

public interface LectureDescriptionRepository extends GenericRepository<LectureDescription> {

    LectureDescription getByTimeTableDescription(TimeTableDescription timeTableDescription);
}
