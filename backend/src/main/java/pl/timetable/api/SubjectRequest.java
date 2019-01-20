package pl.timetable.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SubjectRequest extends BaseRequest {

    private String name;
    private SubjectRequest data;

    public SubjectRequest getData() {
        return data;
    }

    public void setData(SubjectRequest data) {
        this.data = data;
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
                .toString();
    }
}
