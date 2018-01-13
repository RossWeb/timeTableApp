package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Subject;

@Repository
public class SubjectRepositoryImpl extends AbstractGenericRepositoryWithSession<Subject> implements SubjectRepository {
}
