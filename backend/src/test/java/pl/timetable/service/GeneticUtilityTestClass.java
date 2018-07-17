package pl.timetable.service;

import pl.timetable.dto.*;
import pl.timetable.entity.Course;
import pl.timetable.entity.Subject;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GeneticUtilityTestClass {

    private static Supplier<RoomDto> supplierRoom = RoomDto::new;
    private static Supplier<GroupDto> supplierGroup = GroupDto::new;
    private static Supplier<SubjectDto> supplierSubject = SubjectDto::new;
    private static Supplier<CourseDto> supplierCourse = CourseDto::new;

    public static GeneticInitialData getGeneticInitialDataParametrized(Lecture lecture,
                                                                       Integer roomSize,
                                                                       Integer subjectSize,
                                                                       Integer courseSize,
                                                                       Integer groupSize) {


        GeneticInitialData geneticInitialData = new GeneticInitialData();
        geneticInitialData.setLecture(lecture);
        geneticInitialData.setRoomDtoList(getDtoList(supplierRoom, roomSize));
        geneticInitialData.setSubjectDtoList(getDtoList(supplierSubject, subjectSize));
        geneticInitialData.setCourseDtoList(getDtoList(supplierCourse, courseSize).stream().map(courseDto ->
                fillSubjectToCourse(geneticInitialData.getSubjectDtoList(), courseDto, subjectSize)).collect(Collectors.toList()));
        geneticInitialData.setGroupDtoList(getDtoList(supplierGroup, groupSize).stream()
                .map(groupDto -> fillCourseToGroup(geneticInitialData.getCourseDtoList(), groupDto)).collect(Collectors.toList()));
        return geneticInitialData;
    }

    public static GeneticInitialData getGeneticInitialData() {


        GeneticInitialData geneticInitialData = new GeneticInitialData();
        geneticInitialData.setLecture(new Lecture(5, Lecture.REGULAR, 5));
        geneticInitialData.setRoomDtoList(getDtoList(supplierRoom, 10));
        geneticInitialData.setSubjectDtoList(getDtoList(supplierSubject, 10));
        geneticInitialData.setCourseDtoList(getDtoList(supplierCourse, 5).stream().map(courseDto ->
        fillSubjectToCourse(geneticInitialData.getSubjectDtoList(), courseDto, 5)).collect(Collectors.toList()));
        geneticInitialData.setGroupDtoList(getDtoList(supplierGroup, 4).stream()
                .map(groupDto -> fillCourseToGroup(geneticInitialData.getCourseDtoList(), groupDto)).collect(Collectors.toList()));
        return geneticInitialData;
    }

    private static CourseDto fillSubjectToCourse(List<SubjectDto> subjectDtoList, CourseDto courseDto, Integer subjectSize){
        Set<Subject> subjectDtos = new HashSet<>();
        do {
            Integer subjectNumber = new Random().ints(0, subjectDtoList.size()).findFirst().getAsInt();
            subjectDtos.add(mapSubjectDtoToSubject(subjectDtoList.get(subjectNumber)));
        } while (subjectDtos.size() < subjectSize);

        courseDto.setSubjectSet(subjectDtos);
        return courseDto;
    }

    private static GroupDto fillCourseToGroup(List<CourseDto> courseDtoList, GroupDto groupDto){
        Integer courseNumber = new Random().ints(0, courseDtoList.size()).findFirst().getAsInt();
        groupDto.setCourse(mapCourseDtoToCourse(courseDtoList.get(courseNumber)));
        return groupDto;
    }

    private static Course mapCourseDtoToCourse(CourseDto courseDto){
        Course course = new Course();
        course.setSubjectSet(courseDto.getSubjectSet());
        course.setName(courseDto.getName());
        return course;
    }
    private static Subject mapSubjectDtoToSubject(SubjectDto subjectDto){
        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
        subject.setSize(subjectDto.getSize());
        subject.setId(subjectDto.getId());
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
