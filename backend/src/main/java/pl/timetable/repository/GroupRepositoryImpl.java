package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Group;

@Repository
public class GroupRepositoryImpl extends AbstractGenericRepositoryWithSession<Group> implements GroupRepository {

}
