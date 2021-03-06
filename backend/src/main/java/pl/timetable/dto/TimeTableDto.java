package pl.timetable.dto;

import pl.timetable.entity.*;

public class TimeTableDto {

    private Integer lectureNumber;
    private Integer day;
    private Room room;
    private Group group;
    private Subject subject;
    private Teacher teacher;
    private TimeTableDescription timeTableDescription;

    public Integer getLectureNumber() {
        return lectureNumber;
    }

    public void setLectureNumber(Integer lectureNumber) {
        this.lectureNumber = lectureNumber;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public TimeTableDescription getTimeTableDescription() {
        return timeTableDescription;
    }

    public void setTimeTableDescription(TimeTableDescription timeTableDescription) {
        this.timeTableDescription = timeTableDescription;
    }
}
