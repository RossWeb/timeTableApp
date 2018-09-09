package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Course;
import pl.timetable.entity.Subject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CourseRepositoryImpl extends AbstractGenericRepositoryWithSession<Course> implements CourseRepository {

    public List<Subject> getParameters(Integer id){
        Course course = getById(id);
        Set<Subject> subjectRootSet = course.getSubjectSet();
        List<Subject> subjectSet = getSession().createCriteria(Subject.class).list();
//        subjectSet = subjectSet.stream().filter(subjectRootSet::contains).collect(Collectors.toList());
        subjectSet.stream().filter(subjectRootSet::contains).forEach(subject -> subject.setExists(true));
        return subjectSet;
    }

    public List<Subject> findParameters(Integer id, Integer first, Integer max){
        Course course = getById(id);
        Set<Subject> subjectRootSet = course.getSubjectSet();
        List<Subject> subjectSet = getSession().createCriteria(Subject.class)
                .setFirstResult(first)
                .setMaxResults(max)
                .list();
        subjectSet.stream().filter(subjectRootSet::contains).forEach(subject -> subject.setExists(true));
        return subjectSet;
    }

    public Course getCourseByName(String courseName){
        return (Course) getSession().getNamedQuery("findCourseByName").setParameter("name", courseName).uniqueResult();
    }
}
