package pl.timetable.repository;

import pl.timetable.entity.Course;
import pl.timetable.entity.Subject;
import pl.timetable.entity.Teacher;

import java.util.List;

public interface TeacherRepository extends GenericRepository<Teacher> {

    List<Subject> findParameters(Integer id, Integer first, Integer max);
    List<Subject> getParameters(Integer id);
}
