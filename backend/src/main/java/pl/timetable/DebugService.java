package pl.timetable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.dto.*;
import pl.timetable.entity.*;
import pl.timetable.repository.*;
import pl.timetable.service.CourseServiceImpl;
import pl.timetable.service.RoomServiceImpl;
import pl.timetable.service.SubjectServiceImpl;
import pl.timetable.service.TeacherServiceImpl;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class DebugService {

    private static Log Logger = LogFactory.getLog(MyApplication.class);

    private CourseRepository courseRepository;

    private SubjectRepository subjectRepository;

    private GroupRepository groupRepository;

    private RoomRepository roomRepository;

    private HoursLectureRepository hoursLectureRepository;

    private TeacherRepository teacherRepository;

    @Autowired
    public DebugService(CourseRepository courseRepository,
                        SubjectRepository subjectRepository,
                        RoomRepository roomRepository,
                        HoursLectureRepository hoursLectureRepository,
                        TeacherRepository teacherRepository,
                        GroupRepository groupRepository) {
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.hoursLectureRepository = hoursLectureRepository;
        this.teacherRepository = teacherRepository;
        this.roomRepository = roomRepository;
    }

    public DebugService() {
    }

    public void setTeacherRepository(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
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

    public void init(GeneticInitialData geneticInitialData) {
        geneticInitialData.getRoomDtoList().forEach(roomDto -> {
            roomDto.setId(createRoom(roomDto).getId());
        });
        geneticInitialData.getSubjectDtoList().forEach(subjectDto -> {
            subjectDto.setId(createSubject(subjectDto).getId());
        });
        geneticInitialData.getTeacherDtoList().forEach(teacherDto -> {
            Set<Subject> subjectSet = new HashSet<>();
            teacherDto.getSubjectSet().forEach(subject -> {
                Subject subjectFromDataBase = subjectRepository.getSubjectByName(subject.getName());
                subjectSet.add(subjectFromDataBase);
            });
            teacherDto.setSubjectSet(subjectSet);
            teacherDto.setId(createTeacher(teacherDto).getId());
        });

        geneticInitialData.setSubjectDtoList(subjectRepository.findAll()
                .map(subjects -> subjects.stream().map(SubjectServiceImpl::mapEntityToDto).collect(Collectors.toList())).get());
        geneticInitialData.getCourseDtoList().forEach(courseDto -> {
            Set<Subject> subjectSet = new HashSet<>();
            courseDto.getSubjectSet().forEach(subject -> {
                Subject subjectFromDataBase = subjectRepository.getSubjectByName(subject.getName());
                subjectSet.add(subjectFromDataBase);
            });
            courseDto.setSubjectSet(subjectSet);
            courseDto.setId(createCourse(subjectSet, courseDto.getName()).getId());
        });
        geneticInitialData.getGroupDtoList().forEach(groupDto -> {
            Course course = courseRepository.getCourseByName(groupDto.getCourse().getName());
            groupDto.setCourse(course);
            groupDto.setId(createGroup(course, groupDto.getName()));
        });
        Logger.info("da");
    }

    public void init() {
        Logger.info("Fill datatable default value");
        Set<Subject> subjectSet = new HashSet<>();
        Set<Subject> allSubjects = new HashSet<>();
        Set<Course> courseSet = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            Subject subject1 = createSubject("Matematyka" + i);
            Subject subject2 = createSubject("Polski" + i);
            Subject subject3 = createSubject("Fizyka" + i);
            Subject subject4 = createSubject("Biologia" + i);
            subjectSet.add(subject1);
            allSubjects.add(subject1);
            subjectSet.add(subject2);
            allSubjects.add(subject2);
            subjectSet.add(subject3);
            allSubjects.add(subject3);
            subjectSet.add(subject4);
            allSubjects.add(subject4);

        }
        for (int i = 0; i < 2; i++) {
            Set<Subject> subjectCourse = new HashSet<>();
            do {
                Integer position = new Random().ints(0, subjectSet.size()).findFirst().getAsInt();
                Subject subject = (Subject) subjectSet.toArray()[position];
                if (!subjectCourse.contains(subject)) {
                    subjectCourse.add(subject);
                }
            } while (subjectCourse.size() <= 4);
            Course course = createCourse(subjectCourse, "Informatyka" + i);
            courseSet.add(course);
        }
        for (int i = 0; i < 4; i++) {
            Integer position = new Random().ints(0, courseSet.size()).findFirst().getAsInt();
            createGroup((Course) courseSet.toArray()[position], "Informatyka " + i);
        }
        for (int i = 0; i < 10; i++) {
            createRoom("room" + i, String.valueOf(i));
        }

        for (int i = 0; i < 4; i++) {
            HoursLecture hoursLecture = new HoursLecture();
            LocalTime localTime = LocalTime.now();
            if (i != 0) {
                localTime = localTime.plusHours(i);
            }
            hoursLecture.setStartLectureTime(localTime);
            hoursLecture.setPosition(i + 1);
            hoursLectureRepository.create(hoursLecture);
        }

        final String surname1 = "Kowalski";
        final String surname2 = "Malinowski";
        final String name1 = "Adam";
        final String name2 = "Jan";

        for (int i = 0; i < 10; i++) {
            Teacher teacher = new Teacher();
            teacher.setSurname((new Random().nextBoolean() ? surname1 : surname2) + " " + i);
            teacher.setName((new Random().nextBoolean() ? name1 : name2) + " " + i);
            createTeacher(teacher, allSubjects);
        }

    }

    private Teacher createTeacher(Teacher teacher, Set<Subject> allSubjects) {
        Set<Subject> subjectSetTeacher = new HashSet<>();
        do {
            Integer position = new Random().ints(0, allSubjects.size()).findFirst().getAsInt();
            Subject subject = (Subject) allSubjects.toArray()[position];
            if (!subjectSetTeacher.contains(subject)) {
                subjectSetTeacher.add(subject);
            }
        } while (subjectSetTeacher.size() <= 3);

        teacher.setSubjectSet(subjectSetTeacher);
        teacherRepository.create(teacher);
        for (Subject subject : subjectSetTeacher) {
            subject.getTeachers().add(teacher);
            subjectRepository.update(subject);
        }
        return teacher;
    }

    private Integer createRoom(String name, String number) {
        Room room = new Room();
        room.setNumber(number);
        room.setName(name);
        return roomRepository.create(room).getId();
    }

    private Integer createGroup(Course course, String name) {
        Group group = new Group();
        group.setCourse(course);
        group.setName(name);
        return groupRepository.create(group).getId();
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

    private Subject createSubject(SubjectDto subjectDto){
        return subjectRepository.create(SubjectServiceImpl.mapDtoToEntity(subjectDto));
    }

    private Teacher createTeacher(TeacherDto teacherDto){
        return teacherRepository.create(TeacherServiceImpl.mapDtoToEntity(teacherDto));
    }

    private Room createRoom(RoomDto roomDto){
        return roomRepository.create(RoomServiceImpl.mapDtoToEntity(roomDto));
    }

    private Course createCourse(CourseDto courseDto){
        Course course = new Course();
        course.setId(courseDto.getId());
        course.setSubjectSet(courseDto.getSubjectSet());
        course.setName(courseDto.getName());
        return courseRepository.create(course);
    }
}
