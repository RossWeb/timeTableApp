package pl.timetable.dto;

import pl.timetable.entity.Subject;

import java.util.HashSet;
import java.util.Set;

public class TeacherDto extends BaseDto {

    private String name;
    private String surname;
    private Set<Subject> subjectSet = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<Subject> getSubjectSet() {
        return subjectSet;
    }

    public void setSubjectSet(Set<Subject> subjectSet) {
        this.subjectSet = subjectSet;
    }
}
