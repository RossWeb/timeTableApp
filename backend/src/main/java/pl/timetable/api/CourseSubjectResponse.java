package pl.timetable.api;

import pl.timetable.dto.SubjectDto;
import pl.timetable.entity.Subject;

import java.util.List;

public class CourseSubjectResponse extends BaseResponse {

    List<Subject> data;

    public List<Subject> getData() {
        return data;
    }

    public void setData(List<Subject> data) {
        this.data = data;
    }
}
