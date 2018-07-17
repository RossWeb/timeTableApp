package pl.timetable.dto;

import java.util.ArrayList;
import java.util.List;

public class Population {

    private Double fitnessScore = 0.0;
    private Integer populationIteration = 1;
    private Genotype[] selectionArray = new Genotype[100];

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
}
