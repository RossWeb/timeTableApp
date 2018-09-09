package pl.timetable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.HoursLectureRequest;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.entity.*;
import pl.timetable.repository.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Transactional
@Service
public class DebugService {

    private static Log Logger = LogFactory.getLog(MyApplication.class);

    private CourseRepository courseRepository;

    private SubjectRepository subjectRepository;

    private GroupRepository groupRepository;

    private RoomRepository roomRepository;

    private HoursLectureRepository hoursLectureRepository;

    @Autowired
    public DebugService(CourseRepository courseRepository,
                        SubjectRepository subjectRepository,
                        RoomRepository roomRepository,
                        HoursLectureRepository hoursLectureRepository,
                        GroupRepository groupRepository) {
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.hoursLectureRepository = hoursLectureRepository;
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
        Set<Subject> subjectSet = new HashSet<>();
        Set<Course> courseSet = new HashSet<>();
        for (int i = 0; i < 3 ; i++) {
            Subject subject1 = createSubject("Matematyka" + i);
            Subject subject2 = createSubject("Polski" + i);
            Subject subject3 = createSubject("Fizyka" + i);
            Subject subject4 = createSubject("Biologia" + i);
            subjectSet.add(subject1);
            subjectSet.add(subject2);
            subjectSet.add(subject3);
            subjectSet.add(subject4);

        }
        for (int i = 0; i < 2 ; i++) {
            Set<Subject> subjectCourse = new HashSet<>();
            do{
                Integer position = new Random().ints(0, subjectSet.size()).findFirst().getAsInt();
                Subject subject = (Subject) subjectSet.toArray()[position];
                if(!subjectCourse.contains(subject)) {
                    subjectCourse.add(subject);
                }
            }while (subjectCourse.size() <= 4);
            Course course = createCourse(subjectCourse, "Informatyka" + i);
            courseSet.add(course);
        }
        for (int i = 0; i < 4 ; i++) {
            Integer position = new Random().ints(0, courseSet.size()).findFirst().getAsInt();
            createGroup((Course)courseSet.toArray()[position], "Informatyka " + i);
        }
        for (int i = 0; i < 10; i++) {
            createRoom("room" + i, String.valueOf(i));
        }

        for (int i = 0; i < 4; i++) {
            HoursLecture hoursLecture = new HoursLecture();
            LocalTime localTime = LocalTime.now();
            if(i != 0){
                localTime = localTime.plusHours(i);
            }
            hoursLecture.setStartLectureTime(localTime);
            hoursLecture.setPosition(i+1);
            hoursLectureRepository.create(hoursLecture);
        }
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
        subject.setSize(5);
        return subjectRepository.create(subject);
    }
}
