package pl.timetable.entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "findTimeTableByDays",
                query = "from TimeTable tt where tt.day in (:days)"
        )
})
public class TimeTable extends BaseEntity {

    private Integer lectureNumber;
    private Integer day;
    private Room room;
    private Group group;
    private Subject subject;
    private TimeTableDescription timeTableDescription;

    @ManyToOne
    public TimeTableDescription getTimeTableDescription() {
        return timeTableDescription;
    }

    public void setTimeTableDescription(TimeTableDescription timeTableDescription) {
        this.timeTableDescription = timeTableDescription;
    }

    @Column(name = "lecture", nullable = false)
    public Integer getLectureNumber() {
        return lectureNumber;
    }

    public void setLectureNumber(Integer lectureNumber) {
        this.lectureNumber = lectureNumber;
    }

    @Column(name = "day", nullable = false)
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @ManyToOne
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @ManyToOne
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @ManyToOne
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
