package pl.timetable.entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "findReportPopulationByTimeTableDescription",
                query = "from ReportPopulation rp where rp.timeTableDescription.id = :timeTableDescriptionId"
        )
})
public class ReportPopulation extends BaseEntity {

    private TimeTableDescription timeTableDescription;
    private Integer populationSize;
    private Double bestFitnessScore;
    private Integer populationGeneration;
    private Double bestHardFitnessScore;
    private Double bestSoftFitnessScore;

    @ManyToOne
    public TimeTableDescription getTimeTableDescription() {
        return timeTableDescription;
    }

    public void setTimeTableDescription(TimeTableDescription timeTableDescription) {
        this.timeTableDescription = timeTableDescription;
    }
    @Column(name = "populationSize", nullable = false)
    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    @Column(name = "bestFitnessScore", nullable = false)
    public Double getBestFitnessScore() {
        return bestFitnessScore;
    }

    public void setBestFitnessScore(Double bestFitnessScore) {
        this.bestFitnessScore = bestFitnessScore;
    }

    @Column(name = "populationGeneration", nullable = false)
    public Integer getPopulationGeneration() {
        return populationGeneration;
    }

    public void setPopulationGeneration(Integer populationGeneration) {
        this.populationGeneration = populationGeneration;
    }

    @Column(name = "bestHardFitnessScore", nullable = false)
    public Double getBestHardFitnessScore() {
        return bestHardFitnessScore;
    }

    public void setBestHardFitnessScore(Double bestHardFitnessScore) {
        this.bestHardFitnessScore = bestHardFitnessScore;
    }

    @Column(name = "bestSoftFitnessScore", nullable = false)
    public Double getBestSoftFitnessScore() {
        return bestSoftFitnessScore;
    }

    public void setBestSoftFitnessScore(Double bestSoftFitnessScore) {
        this.bestSoftFitnessScore = bestSoftFitnessScore;
    }
}