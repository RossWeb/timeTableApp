package pl.timetable.api;

import pl.timetable.dto.RoomDto;
import pl.timetable.dto.SubjectDto;
import pl.timetable.dto.TeacherDto;

public class TimeTableResponse {

    private Integer lectureNumber;
    private RoomDto room;
    private SubjectDto subject;
    private TeacherDto teacher;
    private Integer day;

    public TeacherDto getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDto teacher) {
        this.teacher = teacher;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getLectureNumber() {
        return lectureNumber;
    }

    public void setLectureNumber(Integer lectureNumber) {
        this.lectureNumber = lectureNumber;
    }

    public RoomDto getRoom() {
        return room;
    }

    public void setRoom(RoomDto room) {
        this.room = room;
    }

    public SubjectDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }
}
