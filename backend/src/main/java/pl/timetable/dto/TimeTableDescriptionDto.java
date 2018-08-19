package pl.timetable.dto;

import java.time.LocalDateTime;

public class TimeTableDescriptionDto extends BaseDto {

    private String name;
    private LocalDateTime createdDate;

    public TimeTableDescriptionDto(String name, Integer id, LocalDateTime createdDate) {
        super.setId(id);
        this.name = name;
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
