package pl.timetable.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Subject extends BaseEntity{

    private String name;

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
