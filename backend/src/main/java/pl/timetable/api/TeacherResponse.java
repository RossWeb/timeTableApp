package pl.timetable.api;

import pl.timetable.dto.TeacherDto;

import java.util.List;

public class TeacherResponse extends BaseResponse {

    private List<TeacherDto> data;

    public List<TeacherDto> getData() {
        return data;
    }

    public void setData(List<TeacherDto> data) {
        this.data = data;
    }
}
