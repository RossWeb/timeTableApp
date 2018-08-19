package pl.timetable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.entity.Course;
import pl.timetable.entity.Group;
import pl.timetable.entity.Room;
import pl.timetable.entity.Subject;
import pl.timetable.repository.CourseRepository;
import pl.timetable.repository.GroupRepository;
import pl.timetable.repository.RoomRepository;
import pl.timetable.repository.SubjectRepository;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class DebugService {

    private static Log Logger = LogFactory.getLog(MyApplication.class);

    private CourseRepository courseRepository;

    private SubjectRepository subjectRepository;

    private GroupRepository groupRepository;

    private RoomRepository roomRepository;



    @Autowired
    public DebugService(CourseRepository courseRepository,
                        SubjectRepository subjectRepository,
                        RoomRepository roomRepository,
                        GroupRepository groupRepository) {
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.roomRepository = roomRepository;
    }

    public DebugService() {
    }

    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void setSubjectRepository(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void init(GeneticInitialData geneticInitialData){
        geneticInitialData.getRoomDtoList().forEach(roomDto -> createRoom(roomDto.getName(), roomDto.getNumber()));
        geneticInitialData.getSubjectDtoList().forEach(subjectDto -> createSubject(subjectDto.getName()));
        geneticInitialData.getCourseDtoList().forEach(courseDto -> {
            Set<Subject> subjectSet = new HashSet<>();
            courseDto.getSubjectSet().forEach(subject -> subjectSet.add(subjectRepository.getSubjectByName(subject.getName())));
            createCourse(subjectSet, courseDto.getName());
        });
        geneticInitialData.getGroupDtoList().forEach(groupDto -> {
            Course course = courseRepository.getCourseByName(groupDto.getCourse().getName());
            createGroup(course, groupDto.getName());
        });
    }

    public void init() {
        Logger.info("Fill datatable default value");
        Subject subject = createSubject("Matematyka");
        Set<Subject> subjectSet = new HashSet<>();
        subjectSet.add(subject);
        Course course = createCourse(subjectSet, "Informatyka");
        createGroup(course, "Informatyka 1");
        createRoom("room1", "1");
    }

    private void createRoom(String name, String number){
        Room room = new Room();
        room.setNumber(number);
        room.setName(name);
        roomRepository.create(room);
    }

    private void createGroup(Course course, String name) {
        Group group = new Group();
        group.setCourse(course);
        group.setName(name);
        groupRepository.create(group);
    }

    private Course createCourse(Set<Subject> subjectSet, String name) {
        Course course = new Course();
        course.setName(name);
        course.setSubjectSet(subjectSet);
        return courseRepository.create(course);
    }

    private Subject createSubject(String name) {
        Subject subject = new Subject();
        subject.setName(name);
        return subjectRepository.create(subject);
    }
}
