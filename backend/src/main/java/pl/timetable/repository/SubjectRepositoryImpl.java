package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Subject;

@Repository
public class SubjectRepositoryImpl extends AbstractGenericRepositoryWithSession<Subject> implements SubjectRepository {

    public Subject getSubjectByName(String subjectName){
        return (Subject) getSession().getNamedQuery("findSubjectByName").setParameter("name", subjectName).uniqueResult();
    }
}
