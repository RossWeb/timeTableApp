package pl.timetable.api;

import java.util.List;

public class HoursLectureRequest extends BaseRequest {

    private String startLectureTime;

    private Integer position;

    private HoursLectureRequest data;

    public HoursLectureRequest getData() {
        return data;
    }

    public void setData(HoursLectureRequest data) {
        this.data = data;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getStartLectureTime() {
        return startLectureTime;
    }

    public void setStartLectureTime(String startLectureTime) {
        this.startLectureTime = startLectureTime;
    }
}
