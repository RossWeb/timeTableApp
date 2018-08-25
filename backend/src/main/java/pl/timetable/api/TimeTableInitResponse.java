package pl.timetable.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TimeTableInitResponse extends BaseResponse {

    private Integer timeTableId;

    public Integer getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(Integer timeTableId) {
        this.timeTableId = timeTableId;
    }

    public TimeTableInitResponse(Integer timeTableId) {
        this.timeTableId = timeTableId;
    }

    public TimeTableInitResponse(String errorMessage) {
        super();
        setErrorMessage(errorMessage);
    }
}
