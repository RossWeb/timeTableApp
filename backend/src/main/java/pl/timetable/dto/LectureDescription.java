package pl.timetable.dto;

public class LectureDescription {

    public final static Integer WEEKLY = 2;
    public final static Integer REGULAR = 5;

    private Integer numberPerDay;
    private Integer daysPerWeek;
    private Integer weeksPerSemester;


    public LectureDescription(Integer numberPerDay, Integer daysPerWeek, Integer weeksPerSemester) {
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
}
