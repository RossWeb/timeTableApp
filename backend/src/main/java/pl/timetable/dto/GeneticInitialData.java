package pl.timetable.dto;

import java.util.List;

public class GeneticInitialData {

    private LectureDescription lectureDescription;
    private List<CourseDto> courseDtoList;
    private List<GroupDto> groupDtoList;
    private List<RoomDto> roomDtoList;
    private List<SubjectDto> subjectDtoList;
    private Integer populationSize;
    private Double mutationValue;

    public Double getMutationValue() {
        return mutationValue;
    }

    public void setMutationValue(Double mutationValue) {
        this.mutationValue = mutationValue;
    }

    public LectureDescription getLectureDescription() {
        return lectureDescription;
    }

    public void setLectureDescription(LectureDescription lectureDescription) {
        this.lectureDescription = lectureDescription;
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
}
