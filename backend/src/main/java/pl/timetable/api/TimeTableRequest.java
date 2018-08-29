package pl.timetable.api;

public class TimeTableRequest extends BaseRequest {

    private Integer numberPerDay;
    private Integer daysPerWeek;
    private Integer weeksPerSemester;
    private Double mutationValue;
    private Integer populationSize;
    private String startedDate;

    public String getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }

    public Double getMutationValue() {
        return mutationValue;
    }

    public void setMutationValue(Double mutationValue) {
        this.mutationValue = mutationValue;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public Integer getNumberPerDay() {
        return numberPerDay;
    }

    public void setNumberPerDay(Integer numberPerDay) {
        this.numberPerDay = numberPerDay;
    }

    public Integer getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(Integer daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public Integer getWeeksPerSemester() {
        return weeksPerSemester;
    }

    public void setWeeksPerSemester(Integer weeksPerSemester) {
        this.weeksPerSemester = weeksPerSemester;
    }
}
