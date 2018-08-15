package pl.timetable.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Population {

    private Double fitnessScore = 0.0;
    private Genotype bestGenotype;
    private Integer populationIteration = 1;
    private Genotype[] selectionArray = new Genotype[100];
    private LectureDescription lectureDescription;

    public LectureDescription getLectureDescription() {
        return lectureDescription;
    }

    public void setLectureDescription(LectureDescription lectureDescription) {
        this.lectureDescription = lectureDescription;
    }

    public Genotype[] getSelectionArray() {
        return selectionArray;
    }

    public void setSelectionArray(Genotype[] selectionArray) {
        this.selectionArray = selectionArray;
    }

    public Double getFitnessScore() {
        return fitnessScore;
    }

    public void setFitnessScore(Double fitnessScore) {
        this.fitnessScore = fitnessScore;
    }

    public Integer getPopulationIteration() {
        return populationIteration;
    }

    public void setPopulationIteration(Integer populationIteration) {
        this.populationIteration = populationIteration;
    }

    private List<Genotype> genotypePopulation = new ArrayList<>();

    public List<Genotype> getGenotypePopulation() {
        return genotypePopulation;
    }

    public void setGenotypePopulation(List<Genotype> genotypePopulation) {
        this.genotypePopulation = genotypePopulation;
    }

    public Genotype getBestGenotype() {
        return bestGenotype;
    }

    public void setBestGenotype(Genotype bestGenotype) {
        this.bestGenotype = bestGenotype;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fitnessScore", fitnessScore)
                .append("bestGenotype", bestGenotype)
                .append("populationIteration", populationIteration)
                .append("selectionArray", selectionArray)
                .append("lectureDescription", lectureDescription)
                .append("genotypePopulation", genotypePopulation)
                .toString();
    }
}
