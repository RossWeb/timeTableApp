package pl.timetable.entity;

import javax.persistence.*;
@Entity
@NamedQueries({
        @NamedQuery(
                name = "findByTimeTableDescriptionId",
                query = "from LectureDescription ld where ld.timeTableDescription.id = :timeTableId"
        )
})
public class LectureDescription extends BaseEntity {

    private Integer numberPerDay;
    private Integer daysPerWeek;
    private Integer weeksPerSemester;
    private TimeTableDescription timeTableDescription;

    @ManyToOne
    public TimeTableDescription getTimeTableDescription() {
        return timeTableDescription;
    }

    public void setTimeTableDescription(TimeTableDescription timeTableDescription) {
        this.timeTableDescription = timeTableDescription;
    }

    @Column(name = "numberPerDay", nullable = false)
    public Integer getNumberPerDay() {
        return numberPerDay;
    }

    public void setNumberPerDay(Integer numberPerDay) {
        this.numberPerDay = numberPerDay;
    }

    @Column(name = "daysPerWeek", nullable = false)
    public Integer getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(Integer daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    @Column(name = "weeksPerSemester", nullable = false)
    public Integer getWeeksPerSemester() {
        return weeksPerSemester;
    }

    public void setWeeksPerSemester(Integer weeksPerSemester) {
        this.weeksPerSemester = weeksPerSemester;
    }
}
