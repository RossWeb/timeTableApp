package pl.timetable.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Persistent;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course extends BaseEntity implements Serializable{

    private String name;

    private Set<Subject> subjectSet = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "course_subject", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
    public Set<Subject> getSubjectSet() {
        return subjectSet;
    }

    public void setSubjectSet(Set<Subject> subjectSet) {
        this.subjectSet = subjectSet;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
