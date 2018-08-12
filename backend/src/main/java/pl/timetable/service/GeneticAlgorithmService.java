package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pl.timetable.dto.Cell;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.dto.Genotype;
import pl.timetable.dto.Population;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GeneticAlgorithmService {

    private static final Logger LOGGER = Logger.getLogger(GeneticAlgorithmService.class);

    private GenotypeService genotypeService;
    private FitnessService fitnessService;

    public GeneticAlgorithmService(GenotypeService genotypeService, FitnessService fitnessService) {
        this.genotypeService = genotypeService;
        this.fitnessService = fitnessService;
    }

    public void setFitnessService(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
    }

    public void setGenotypeService(GenotypeService genotypeService) {
        this.genotypeService = genotypeService;
    }

    public Population init(GeneticInitialData geneticInitialData){

        //create initial population
        Population population = new Population();
        Double globalFitnessScore =0.0;
        Integer counterSameFitnesScore = 0;
        Integer populationIteration = 0;
        for (int i = 0; i < geneticInitialData.getPopulationSize(); i++) {
            LOGGER.info("Create population : " + i);
            population.getGenotypePopulation().add(genotypeService.createInitialGenotype(geneticInitialData));
        }
//        Population newPopulation = processGenetic(population);
        for (;;) {
            LOGGER.info("Population generation : " + ++populationIteration);
            Population newPopulation = processGenetic(population);
            if(population.getFitnessScore() == 100 || population.getGenotypePopulation().size() == 0 || counterSameFitnesScore == 10){
                break;
            }else{
                if(globalFitnessScore.equals(population.getFitnessScore())){
                    counterSameFitnesScore++;
                }else{
                    globalFitnessScore = population.getFitnessScore();
                }
            }

            //mutate
            //mutate newPopulation
            //create next population
            population = genotypeService.mapHintNewPopulation(newPopulation);
            //end and present score
            LOGGER.info("Max fitness score for generation " + population.getFitnessScore());

        }
        LOGGER.info("Max fitness score : "+ population.getFitnessScore());
        LOGGER.info("Generation end " + populationIteration);
        LOGGER.info("Same fitness score " + counterSameFitnesScore);
        return population;

    }

    private Population processGenetic(Population population) {
        //fitness function
        //check fitness score of population
        fitnessService.fitPopulation(population);
        //selection by roulete
        fitnessService.selectionRoulette(population);
        Genotype genotypeFirst = fitnessService.getGenotypeBySelection(population);
        Genotype genotypeSecond = fitnessService.getGenotypeBySelection(population);
        //crossover
        Population newPopulation = new Population();
        newPopulation.setPopulationIteration(population.getPopulationIteration()+ 1);
        for (int i = 0; i < population.getGenotypePopulation().size()/2; i++) {
            genotypeService.crossover(genotypeFirst, genotypeSecond);
            newPopulation.getGenotypePopulation().add(genotypeFirst);
            newPopulation.getGenotypePopulation().add(genotypeSecond);
        }
        return newPopulation;
    }
}
