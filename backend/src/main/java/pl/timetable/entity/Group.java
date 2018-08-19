package pl.timetable.entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "findGroupByName",
                query = "from Group g where g.name = :name"
        )
})
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

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
