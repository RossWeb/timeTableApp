package pl.timetable.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class GeneticInitialData {

    private LectureDescriptionDto lectureDescriptionDto;
    private List<CourseDto> courseDtoList;
    private List<GroupDto> groupDtoList;
    private List<RoomDto> roomDtoList;
    private List<SubjectDto> subjectDtoList;
    private List<TeacherDto> teacherDtoList;
    private Integer populationSize;
    private Double mutationValue;
    private LocalDateTime startedDate;

    public LocalDateTime getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDateTime startedDate) {
        this.startedDate = startedDate;
    }

    public Double getMutationValue() {
        return mutationValue;
    }

    public void setMutationValue(Double mutationValue) {
        this.mutationValue = mutationValue;
    }

    public LectureDescriptionDto getLectureDescriptionDto() {
        return lectureDescriptionDto;
    }

    public void setLectureDescriptionDto(LectureDescriptionDto lectureDescriptionDto) {
        this.lectureDescriptionDto = lectureDescriptionDto;
    }

    public List<CourseDto> getCourseDtoList() {
        return courseDtoList;
    }

    public void setCourseDtoList(List<CourseDto> courseDtoList) {
        this.courseDtoList = courseDtoList;
    }

    public List<GroupDto> getGroupDtoList() {
        return groupDtoList;
    }

    public void setGroupDtoList(List<GroupDto> groupDtoList) {
        this.groupDtoList = groupDtoList;
    }

    public List<RoomDto> getRoomDtoList() {
        return roomDtoList;
    }

    public void setRoomDtoList(List<RoomDto> roomDtoList) {
        this.roomDtoList = roomDtoList;
    }

    public List<SubjectDto> getSubjectDtoList() {
        return subjectDtoList;
    }

    public void setSubjectDtoList(List<SubjectDto> subjectDtoList) {
        this.subjectDtoList = subjectDtoList;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public List<TeacherDto> getTeacherDtoList() {
        return teacherDtoList;
    }

    public void setTeacherDtoList(List<TeacherDto> teacherDtoList) {
        this.teacherDtoList = teacherDtoList;
    }
}
