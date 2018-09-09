package pl.timetable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

public class HoursLectureDto extends BaseDto {

    @JsonFormat(pattern = "HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startLectureTime;

    private Integer position;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public LocalTime getStartLectureTime() {
        return startLectureTime;
    }

    public void setStartLectureTime(LocalTime startLectureTime) {
        this.startLectureTime = startLectureTime;
    }
}
