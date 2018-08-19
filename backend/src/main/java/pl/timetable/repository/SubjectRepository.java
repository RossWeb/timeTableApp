package pl.timetable.repository;

import pl.timetable.entity.Subject;

public interface SubjectRepository extends GenericRepository<Subject> {
    public Subject getSubjectByName(String subjectName);
}
