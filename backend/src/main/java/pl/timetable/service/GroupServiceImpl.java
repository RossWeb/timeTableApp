package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.GroupRequest;
import pl.timetable.entity.Course;
import pl.timetable.entity.Group;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.CourseRepository;
import pl.timetable.repository.GroupRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class GroupServiceImpl extends AbstractService<Group, GroupRequest> {

    public static final Logger LOGGER = Logger.getLogger(GroupServiceImpl.class);

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseServiceImpl courseService;

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll().orElse(Collections.emptyList());
    }

    @Override
    public void create(GroupRequest groupRequest) throws EntityNotFoundException {
        Group group = mapRequestToEntity(groupRequest);
        if(Objects.nonNull(groupRequest.getCourseId())) {
            Course course = courseService.get(groupRequest.getCourseId());
            group.setCourse(course);
        }
        groupRepository.create(group);
    }

    @Override
    public Group update(GroupRequest groupRequest, int id) {
        Group entity = mapRequestToEntity(groupRequest);
        entity.setId(id);
        return groupRepository.update(entity);
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        Group entity = get(id);
        groupRepository.remove(entity);
    }

    @Override
    public Group get(int id) throws EntityNotFoundException {
        return Optional.ofNullable(groupRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Group"));
    }

    private Group mapRequestToEntity(GroupRequest groupRequest) {
        Group entity = new Group();
        entity.setName(groupRequest.getName());
        return entity;
    }
}
