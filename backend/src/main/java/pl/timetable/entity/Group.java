package pl.timetable.entity;

import javax.persistence.*;

@Entity
public class Group extends BaseEntity {

    private String name;

    private Course course;

    @ManyToOne
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
