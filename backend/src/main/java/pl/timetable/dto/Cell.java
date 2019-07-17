package pl.timetable.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Cell implements Cloneable {

    private Integer lecture;
    private RoomDto roomDto;
    private CourseDto courseDto;
    private SubjectDto subjectDto;
    private GroupDto groupDto;
    private TeacherDto teacherDto;
    private Integer day;

    public Cell(Integer lecture, CourseDto courseDto, GroupDto groupDto, Integer day) {
        this.lecture = lecture;
        this.courseDto = courseDto;
        this.groupDto = groupDto;
        this.day = day;
    }

    public TeacherDto getTeacherDto() {
        return teacherDto;
    }

    public void setTeacherDto(TeacherDto teacherDto) {
        this.teacherDto = teacherDto;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
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
    public Cell clone() throws CloneNotSupportedException {
        super.clone();
        Cell cell = new Cell(this.lecture, this.courseDto, this.groupDto, this.day);
        cell.setTeacherDto(this.teacherDto);
        cell.setRoomDto(this.roomDto);
        cell.setSubjectDto(this.subjectDto);
        return cell;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lecture", lecture)
                .append("day", day)
                .append("roomDto", roomDto)
                .append("courseDto", courseDto)
                .append("subjectDto", subjectDto)
                .append("groupDto", groupDto)
                .toString();
    }
}
