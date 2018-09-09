package pl.timetable.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.timetable.entity.Course;

public class CourseRequest extends BaseRequest {

    private String name;
    private Integer subject;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("subject", subject)
                .toString();
    }
}
