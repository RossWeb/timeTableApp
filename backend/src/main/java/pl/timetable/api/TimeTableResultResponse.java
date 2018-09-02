package pl.timetable.api;

import pl.timetable.dto.TimeTableDto;

import java.util.List;

public class TimeTableResultResponse extends BaseResponse {


    List<TimeTableResponse> data;

    public List<TimeTableResponse> getData() {
        return data;
    }

    public void setData(List<TimeTableResponse> data) {
        this.data = data;
    }
}
