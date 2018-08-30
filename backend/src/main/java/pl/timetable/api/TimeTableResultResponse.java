package pl.timetable.api;

import pl.timetable.dto.TimeTableDto;

import java.util.List;

public class TimeTableResultResponse extends BaseResponse {


    List<TimeTableDto> data;

    public List<TimeTableDto> getData() {
        return data;
    }

    public void setData(List<TimeTableDto> data) {
        this.data = data;
    }
}
