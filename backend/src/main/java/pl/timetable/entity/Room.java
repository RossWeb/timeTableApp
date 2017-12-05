package pl.timetable.entity;

import pl.timetable.api.BaseResponse;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Room extends BaseEntity {

    private String name;
    private String number;

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "number", nullable = false)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
