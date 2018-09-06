package pl.timetable.api;

import pl.timetable.dto.RoomDto;

import java.util.List;

public class RoomResponse extends BaseResponse {

    List<RoomDto> data;

    public List<RoomDto> getData() {
        return data;
    }

    public void setData(List<RoomDto> data) {
        this.data = data;
    }
}
