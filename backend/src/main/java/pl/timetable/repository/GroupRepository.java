package pl.timetable.repository;

import pl.timetable.entity.Group;

public interface GroupRepository extends GenericRepository<Group> {
    public Group getGroupByName(String groupName);

}
