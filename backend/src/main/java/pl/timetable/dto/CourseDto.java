package pl.timetable.dto;

import pl.timetable.entity.Subject;

import java.util.HashSet;
import java.util.Set;

public class CourseDto {

    private Integer id;
    private String name;
    private Set<Subject> subjectSet = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Subject> getSubjectSet() {
        return subjectSet;
    }

    public void setSubjectSet(Set<Subject> subjectSet) {
        this.subjectSet = subjectSet;
    }
}
