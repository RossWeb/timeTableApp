package pl.timetable.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RoomDto extends BaseDto{

    private String name;
    private String number;

    public RoomDto() {
        //empty constructor
    }

    public RoomDto(String name, String number, Integer id) {
        super.setId(id);
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof RoomDto)) return false;

        RoomDto roomDto = (RoomDto) o;

        return new EqualsBuilder()
                .append(super.getId(), roomDto.getId())
                .append(name, roomDto.name)
                .append(number, roomDto.number)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(super.getId())
                .append(name)
                .append(number)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("name", name)
                .append("number", number)
                .toString();
    }
}
