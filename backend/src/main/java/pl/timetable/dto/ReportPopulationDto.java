package pl.timetable.dto;

public class ReportPopulationDto extends BaseDto {

    private Integer timeTableDescriptionId;
    private Integer populationSize;
    private Double bestFitnessScore;
    private Integer populationGeneration;
    private Double bestHardFitnessScore;
    private Double bestSoftFitnessScore;

    public Integer getTimeTableDescriptionId() {
        return timeTableDescriptionId;
    }

    public void setTimeTableDescriptionId(Integer timeTableDescriptionId) {
        this.timeTableDescriptionId = timeTableDescriptionId;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }


    public Integer getPopulationGeneration() {
        return populationGeneration;
    }

    public void setPopulationGeneration(Integer populationGeneration) {
        this.populationGeneration = populationGeneration;
    }

    public Double getBestFitnessScore() {
        return bestFitnessScore;
    }

    public void setBestFitnessScore(Double bestFitnessScore) {
        this.bestFitnessScore = bestFitnessScore;
    }

    public Double getBestHardFitnessScore() {
        return bestHardFitnessScore;
    }

    public void setBestHardFitnessScore(Double bestHardFitnessScore) {
        this.bestHardFitnessScore = bestHardFitnessScore;
    }

    public Double getBestSoftFitnessScore() {
        return bestSoftFitnessScore;
    }

    public void setBestSoftFitnessScore(Double bestSoftFitnessScore) {
        this.bestSoftFitnessScore = bestSoftFitnessScore;
    }
}
