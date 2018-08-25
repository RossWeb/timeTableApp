package pl.timetable.dto;

import pl.timetable.entity.TimeTableDescription;
import pl.timetable.enums.TimeTableDescriptionStatus;

import java.time.LocalDateTime;

public class TimeTableDescriptionDto extends BaseDto {

    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime startedDate;
    private TimeTableDescriptionStatus status;

    public TimeTableDescriptionDto(String name, Integer id, LocalDateTime createdDate, LocalDateTime startedDate) {
        super.setId(id);
        this.name = name;
        this.createdDate = createdDate;
        this.startedDate = startedDate;
        this.status = TimeTableDescriptionStatus.PENDING;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getStartedDate() {
        return startedDate;
    }

    public TimeTableDescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(TimeTableDescriptionStatus status) {
        this.status = status;
    }
}
