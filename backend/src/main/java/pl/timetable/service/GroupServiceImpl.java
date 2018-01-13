package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.GroupRequest;
import pl.timetable.dto.GroupDto;
import pl.timetable.entity.Course;
import pl.timetable.entity.Group;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.CourseRepository;
import pl.timetable.repository.GroupRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupServiceImpl extends AbstractService<GroupDto, GroupRequest> {

    public static final Logger LOGGER = Logger.getLogger(GroupServiceImpl.class);

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<GroupDto> findAll() {
        List<Group> groupEntities = groupRepository.findAll().orElse(Collections.emptyList());
        return groupEntities.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void create(GroupRequest groupRequest) throws EntityNotFoundException {
        Group group = mapRequestToEntity(groupRequest);
        if (Objects.nonNull(groupRequest.getCourseId())) {
            group.setCourse(getCourse(groupRequest.getCourseId()));
        }
        groupRepository.create(group);
    }

    @Override
    public GroupDto update(GroupRequest groupRequest, int id) {
        Group entity = mapRequestToEntity(groupRequest);
        entity.setId(id);
        return mapEntityToDto(groupRepository.update(entity));
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        Group group = Optional.ofNullable(groupRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Group"));
        groupRepository.remove(group);
    }

    @Override
    public GroupDto get(int id) throws EntityNotFoundException {
        return mapEntityToDto(Optional.ofNullable(groupRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Group")));
    }

    private Course getCourse(Integer courseId) throws EntityNotFoundException {
        return Optional.ofNullable(courseRepository.getById(courseId))
                .orElseThrow(() -> new EntityNotFoundException(courseId, "Course"));
    }

    private GroupDto mapEntityToDto(Group group) {
        GroupDto dto = new GroupDto();
        dto.setCourse(group.getCourse());
        dto.setId(group.getId());
        dto.setName(group.getName());
        return dto;
    }


    private Group mapRequestToEntity(GroupRequest groupRequest) {
        Group entity = new Group();
        entity.setName(groupRequest.getName());
        return entity;
    }
}
