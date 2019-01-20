package pl.timetable.api;

public class TeacherRequest extends BaseRequest {

    private String name;
    private String surname;
    private Integer subject;
    private Integer id;
    private TeacherRequest data;

    public TeacherRequest getData() {
        return data;
    }

    public void setData(TeacherRequest data) {
        this.data = data;
    }

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
