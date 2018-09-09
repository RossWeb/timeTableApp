package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Subject;
import pl.timetable.entity.Teacher;

import java.util.List;
import java.util.Set;

@Repository
public class TeacherRepositoryImpl extends AbstractGenericRepositoryWithSession<Teacher> implements TeacherRepository {

    @Override
    public List<Subject> findParameters(Integer id, Integer first, Integer max) {
        Teacher teacher = getById(id);
        Set<Subject> subjectRootSet = teacher.getSubjectSet();
        List<Subject> subjectSet = getSession().createCriteria(Subject.class)
                .setFirstResult(first)
                .setMaxResults(max)
                .list();
        subjectSet.stream().filter(subjectRootSet::contains).forEach(subject -> subject.setExists(true));
        return subjectSet;
    }

    @Override
    public List<Subject> getParameters(Integer id) {
        Teacher teacher = getById(id);
        Set<Subject> subjectRootSet = teacher.getSubjectSet();
        List<Subject> subjectSet = getSession().createCriteria(Subject.class).list();
        subjectSet.stream().filter(subjectRootSet::contains).forEach(subject -> subject.setExists(true));
        return subjectSet;
    }
}
