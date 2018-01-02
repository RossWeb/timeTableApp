package pl.timetable.api;

public class GroupRequest extends BaseRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
