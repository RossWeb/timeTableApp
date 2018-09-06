package pl.timetable.api;

import pl.timetable.dto.GroupDto;

import java.util.List;

public class GroupResponse extends BaseResponse {

    List<GroupDto> data;

    public List<GroupDto> getData() {
        return data;
    }

    public void setData(List<GroupDto> data) {
        this.data = data;
    }
}
