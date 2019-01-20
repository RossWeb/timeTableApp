package pl.timetable.service;

import pl.timetable.dto.*;
import pl.timetable.entity.Course;
import pl.timetable.entity.Subject;
import pl.timetable.entity.Teacher;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GeneticUtilityTestClass {

    private static Integer roomNumber = 25;
    private static Integer subjectNumber = 10;
    private static Integer courseNumber = 5;
    private static Integer groupNumber = 4;
    private static Integer teacherNumber = 10;

    private static Supplier<RoomDto> supplierRoom = () -> {
        RoomDto roomDto = new RoomDto();
        roomDto.setName("room" + roomNumber--);
        roomDto.setNumber(String.valueOf(roomNumber));
        return roomDto;
    };
    private static Supplier<GroupDto> supplierGroup = ()->{
        GroupDto groupDto = new GroupDto();
        groupDto.setName("Group" + groupNumber--);
        return groupDto;
    };
    private static Supplier<SubjectDto> supplierSubject = () -> {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setSize(8);
        subjectDto.setName("Subject " + subjectNumber--);
        return subjectDto;
    };
    private static Supplier<CourseDto> supplierCourse = () -> {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Course " + courseNumber--);
        return courseDto;
    };

    private static Supplier<TeacherDto> supplierTeacher = () -> {
        TeacherDto teacherDto = new TeacherDto();
        Integer number = teacherNumber--;
        teacherDto.setName("Jan" + number);
        teacherDto.setSurname("Kowalski" + number);
        return teacherDto;
    };

    public static GeneticInitialData getGeneticInitialDataParametrized(LectureDescriptionDto lectureDescriptionDto,
                                                                       Integer roomSize,
                                                                       Integer subjectSize,
                                                                       Integer courseSize,
                                                                       Integer groupSize,
                                                                       Integer teacherSize) {


        GeneticInitialData geneticInitialData = new GeneticInitialData();
        geneticInitialData.setLectureDescriptionDto(lectureDescriptionDto);
        geneticInitialData.setRoomDtoList(getDtoList(supplierRoom, roomSize));
        geneticInitialData.setSubjectDtoList(getDtoList(supplierSubject, subjectSize));
        geneticInitialData.setCourseDtoList(getDtoList(supplierCourse, courseSize).stream().map(courseDto ->
                fillSubjectToCourse(geneticInitialData.getSubjectDtoList(), courseDto, subjectSize)).collect(Collectors.toList()));
        geneticInitialData.setGroupDtoList(getDtoList(supplierGroup, groupSize).stream()
                .map(groupDto -> fillCourseToGroup(geneticInitialData.getCourseDtoList(), groupDto)).collect(Collectors.toList()));
        geneticInitialData.setTeacherDtoList(getDtoList(supplierTeacher, teacherNumber).stream()
                .map(teacherDto -> fillSubjectToTeacher(geneticInitialData.getSubjectDtoList(), teacherDto, teacherSize)).collect(Collectors.toList()));
        geneticInitialData.setMutationValue(0.15);
        return geneticInitialData;
    }

    public static GeneticInitialData getGeneticInitialData() {


        GeneticInitialData geneticInitialData = new GeneticInitialData();
        geneticInitialData.setLectureDescriptionDto(new LectureDescriptionDto(5, LectureDescriptionDto.REGULAR, 2));
        geneticInitialData.setRoomDtoList(getDtoList(supplierRoom, roomNumber));
        geneticInitialData.setSubjectDtoList(getDtoList(supplierSubject, subjectNumber));
        geneticInitialData.setTeacherDtoList(getDtoList(supplierTeacher, teacherNumber).stream()
                .map(teacherDto -> fillSubjectToTeacher(geneticInitialData.getSubjectDtoList(), teacherDto, 4)).collect(Collectors.toList()));
        geneticInitialData.setCourseDtoList(getDtoList(supplierCourse, courseNumber).stream().map(courseDto ->
                fillSubjectToCourse(geneticInitialData.getSubjectDtoList(), courseDto, 6)).collect(Collectors.toList()));
        geneticInitialData.setGroupDtoList(getDtoList(supplierGroup, groupNumber).stream()
                .map(groupDto -> fillCourseToGroup(geneticInitialData.getCourseDtoList(), groupDto)).collect(Collectors.toList()));
        geneticInitialData.setMutationValue(0.15);
        return geneticInitialData;
    }

    private static CourseDto fillSubjectToCourse(List<SubjectDto> subjectDtoList, CourseDto courseDto, Integer subjectSize) {
        Set<Subject> subjectDtos = new HashSet<>();
        do {
            Integer subjectNumber = new Random().ints(0, subjectDtoList.size()).findFirst().getAsInt();
            subjectDtos.add(mapSubjectDtoToSubject(subjectDtoList.get(subjectNumber)));
        } while (subjectDtos.size() < subjectSize);

        courseDto.setSubjectSet(subjectDtos);
        return courseDto;
    }

    private static GroupDto fillCourseToGroup(List<CourseDto> courseDtoList, GroupDto groupDto) {
        Integer courseNumber = new Random().ints(0, courseDtoList.size()).findFirst().getAsInt();
        groupDto.setCourse(mapCourseDtoToCourse(courseDtoList.get(courseNumber)));
        return groupDto;
    }

    private static TeacherDto fillSubjectToTeacher(List<SubjectDto> subjectDtoList, TeacherDto teacherDto, Integer subjectSize){
        Set<Subject> subjectDtos = new HashSet<>();
        Teacher teacher = TeacherServiceImpl.mapDtoToEntity(teacherDto);
        do {
            Integer subjectNumber = new Random().ints(0, subjectDtoList.size()).findFirst().getAsInt();
            if(subjectDtoList.get(subjectNumber).getTeachers().stream().noneMatch(teacherFounded -> teacherFounded.equals(teacher))) {
                subjectDtoList.get(subjectNumber).getTeachers().add(teacher);
                subjectDtos.add(mapSubjectDtoToSubject(subjectDtoList.get(subjectNumber)));
            }
        } while (subjectDtos.size() < subjectSize);

        teacherDto.setSubjectSet(subjectDtos);
        return teacherDto;
    }

    private static Course mapCourseDtoToCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setSubjectSet(courseDto.getSubjectSet());
        course.setName(courseDto.getName());
        return course;
    }

    private static Subject mapSubjectDtoToSubject(SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
        subject.setSize(subjectDto.getSize());
        subject.setId(subjectDto.getId());
        subject.setTeachers(new HashSet<>(subjectDto.getTeachers()));
        return subject;
    }

    private static <T extends BaseDto> List<T> getDtoList(Supplier<T> supplier, int size) {
        List<T> entityDtos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            T t = supplier.get();
            t.setId(i);
            entityDtos.add(t);
        }
        return entityDtos;
    }

}
