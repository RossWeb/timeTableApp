package pl.timetable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.GroupRequest;
import pl.timetable.api.GroupRequest;
import pl.timetable.entity.Group;
import pl.timetable.entity.Group;
import pl.timetable.repository.GroupRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupServiceImpl extends AbstractService<Group, GroupRequest> {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<Group> findAll() {
        Optional<List<Group>> GroupList = groupRepository.findAll();
        return GroupList.orElse(Collections.emptyList());
    }

    @Override
    public void create(GroupRequest groupRequest) {
        groupRepository.create(mapRequestToEntity(groupRequest));
    }

    @Override
    public Group update(GroupRequest groupRequest, int id) {
        Group entity = mapRequestToEntity(groupRequest);
        entity.setId(id);
        return groupRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        Group entity = get(id);
        groupRepository.remove(entity);
    }

    @Override
    public Group get(int id) {
        return groupRepository.getById(id);
    }

    private Group mapRequestToEntity(GroupRequest groupRequest) {
        Group entity = new Group();
        entity.setName(groupRequest.getName());
        return entity;
    }
}
