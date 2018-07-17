package pl.timetable.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.timetable.entity.Course;

public class GroupDto extends BaseDto{

    private String name;
    private Course course;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("name", name)
                .append("course", course)
                .toString();
    }
}
