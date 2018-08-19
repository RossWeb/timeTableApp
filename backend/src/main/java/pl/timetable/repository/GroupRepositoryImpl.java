package pl.timetable.repository;

import org.springframework.stereotype.Repository;
import pl.timetable.entity.Course;
import pl.timetable.entity.Group;

@Repository
public class GroupRepositoryImpl extends AbstractGenericRepositoryWithSession<Group> implements GroupRepository {

    public Group getGroupByName(String groupName) {
        return (Group) getSession().getNamedQuery("findGroupByName").setParameter("name", groupName).uniqueResult();
    }

}
