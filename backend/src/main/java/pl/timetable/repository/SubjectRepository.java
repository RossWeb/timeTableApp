package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Subject;

@Repository
public interface SubjectRepository extends GenericRepository<Subject> {
}
