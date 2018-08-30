package pl.timetable.dto;

import java.util.List;

public class TimeTableResultDto extends PagingResponseDto{

    private List<TimeTableDto> timeTableDtos;

    public List<TimeTableDto> getTimeTableDtos() {
        return timeTableDtos;
    }

    public void setTimeTableDtos(List<TimeTableDto> timeTableDtos) {
        this.timeTableDtos = timeTableDtos;
    }
}
