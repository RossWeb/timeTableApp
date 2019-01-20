package pl.timetable.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RoomRequest extends BaseRequest {

    private String name;
    private String number;
    private RoomRequest data;

    public RoomRequest getData() {
        return data;
    }

    public void setData(RoomRequest data) {
        this.data = data;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("number", number)
                .toString();
    }
}
