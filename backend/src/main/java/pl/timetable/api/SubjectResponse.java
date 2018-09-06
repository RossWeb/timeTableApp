package pl.timetable.api;

import pl.timetable.dto.SubjectDto;

import java.util.List;

public class SubjectResponse extends BaseResponse {

    List<SubjectDto> data;

    public List<SubjectDto> getData() {
        return data;
    }

    public void setData(List<SubjectDto> data) {
        this.data = data;
    }
}
