package pl.timetable.api;

import pl.timetable.dto.HoursLectureDto;

import java.util.List;

public class HoursLectureResponse extends BaseResponse {

    List<HoursLectureDto> data;

    public List<HoursLectureDto> getData() {
        return data;
    }

    public void setData(List<HoursLectureDto> data) {
        this.data = data;
    }
}
