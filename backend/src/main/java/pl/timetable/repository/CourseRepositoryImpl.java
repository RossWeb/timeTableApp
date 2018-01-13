package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Course;

@Repository
public class CourseRepositoryImpl extends AbstractGenericRepositoryWithSession<Course> implements CourseRepository {

}
