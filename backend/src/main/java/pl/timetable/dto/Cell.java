package pl.timetable.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Cell {

    private Integer lecture;
    private RoomDto roomDto;
    private CourseDto courseDto;
    private SubjectDto subjectDto;
    private GroupDto groupDto;

    public Cell(Integer lecture, RoomDto roomDto, CourseDto courseDto, SubjectDto subjectDto, GroupDto groupDto) {
        this.lecture = lecture;
        this.roomDto = roomDto;
        this.courseDto = courseDto;
        this.subjectDto = subjectDto;
        this.groupDto = groupDto;
    }

    public GroupDto getGroupDto() {
        return groupDto;
    }

    public void setGroupDto(GroupDto groupDto) {
        this.groupDto = groupDto;
    }

    public void setLecture(Integer lecture) {
        this.lecture = lecture;
    }

    public void setRoomDto(RoomDto roomDto) {
        this.roomDto = roomDto;
    }

    public void setCourseDto(CourseDto courseDto) {
        this.courseDto = courseDto;
    }

    public void setSubjectDto(SubjectDto subjectDto) {
        this.subjectDto = subjectDto;
    }

    public Integer getLecture() {
        return lecture;
    }

    public RoomDto getRoomDto() {
        return roomDto;
    }

    public CourseDto getCourseDto() {
        return courseDto;
    }

    public SubjectDto getSubjectDto() {
        return subjectDto;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lecture", lecture)
                .append("roomDto", roomDto)
                .append("courseDto", courseDto)
                .append("subjectDto", subjectDto)
                .append("groupDto", groupDto)
                .toString();
    }
}
