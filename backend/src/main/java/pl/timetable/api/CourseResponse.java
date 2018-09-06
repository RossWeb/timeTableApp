package pl.timetable.api;

import pl.timetable.dto.CourseDto;

import java.util.List;

public class CourseResponse extends BaseResponse {

    List<CourseDto> data;

    public List<CourseDto> getData() {
        return data;
    }

    public void setData(List<CourseDto> data) {
        this.data = data;
    }
}
