package pl.timetable.entity;

import pl.timetable.enums.TimeTableDescriptionStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
public class TimeTableDescription extends BaseEntity {

    private LocalDateTime createdDate;
    private LocalDateTime startedDate;
    private String name;
    private TimeTableDescriptionStatus timeTableDescriptionStatus = TimeTableDescriptionStatus.PENDING;

    @Column(name = "createdDate", nullable = false)
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "startedDate", nullable = false)
    public LocalDateTime getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDateTime startedDate) {
        this.startedDate = startedDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public TimeTableDescriptionStatus getTimeTableDescriptionStatus() {
        return timeTableDescriptionStatus;
    }

    public void setTimeTableDescriptionStatus(TimeTableDescriptionStatus timeTableDescriptionStatus) {
        this.timeTableDescriptionStatus = timeTableDescriptionStatus;
    }
}
