package pl.timetable.dto;

import pl.timetable.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

public class SubjectDto extends BaseDto {

    private String name;
    private Integer size;
    private List<Teacher> teachers = new ArrayList<>();

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
