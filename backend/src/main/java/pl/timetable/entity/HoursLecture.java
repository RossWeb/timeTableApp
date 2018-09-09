package pl.timetable.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalTime;

@Entity
public class HoursLecture extends BaseEntity {


    private LocalTime startLectureTime;

    private Integer position;

    @Column(name = "position", nullable = false)
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Column(name = "startLectureTime", nullable = false)
    public LocalTime getStartLectureTime() {
        return startLectureTime;
    }

    public void setStartLectureTime(LocalTime startLectureTime) {
        this.startLectureTime = startLectureTime;
    }
}
