package pl.timetable.repository;

import pl.timetable.entity.Course;
import pl.timetable.entity.Subject;

import java.util.List;

public interface CourseRepository extends GenericRepository<Course> {

    List<Subject> getParameters(Integer id);
}
