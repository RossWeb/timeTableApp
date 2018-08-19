package pl.timetable.dto;

public class LectureDescriptionDto {

    public final static Integer WEEKLY = 2;
    public final static Integer REGULAR = 5;

    private Integer numberPerDay;
    private Integer daysPerWeek;
    private Integer weeksPerSemester;
    private Integer timeTableDescriptionId;


    public LectureDescriptionDto(Integer numberPerDay, Integer daysPerWeek, Integer weeksPerSemester) {
        this.numberPerDay = numberPerDay;
        this.daysPerWeek = daysPerWeek;
        this.weeksPerSemester = weeksPerSemester;
    }

    public Integer getNumberPerDay() {
        return numberPerDay;
    }

    public Integer getDaysPerWeek() {
        return daysPerWeek;
    }

    public Integer getWeeksPerSemester() {
        return weeksPerSemester;
    }

    public Integer getTimeTableDescriptionId() {
        return timeTableDescriptionId;
    }

    public void setTimeTableDescriptionId(Integer timeTableDescriptionId) {
        this.timeTableDescriptionId = timeTableDescriptionId;
    }
}
