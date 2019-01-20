package pl.timetable.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GroupRequest extends BaseRequest {

    private String name;

    private Integer courseId;
    private GroupRequest data;

    public GroupRequest getData() {
        return data;
    }

    public void setData(GroupRequest data) {
        this.data = data;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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
                .append("courseId", courseId)
                .toString();
    }
}
