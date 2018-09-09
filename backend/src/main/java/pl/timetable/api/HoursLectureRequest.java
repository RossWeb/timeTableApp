package pl.timetable.api;

public class HoursLectureRequest extends BaseRequest {

    private String startLectureTime;

    private Integer position;

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
