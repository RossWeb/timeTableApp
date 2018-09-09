package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.HoursLecture;

@Repository
public class HoursLectureRepositoryImpl extends AbstractGenericRepositoryWithSession<HoursLecture> implements HoursLectureRepository {
}
