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
import java.util.Objects;

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
        Integer counterSameFitnessScore = 0;
//        Integer populationIteration = 0;
        for (int i = 0; i < geneticInitialData.getPopulationSize(); i++) {
            LOGGER.info("Create population : " + i);
            population.getGenotypePopulation().add(genotypeService.createInitialGenotype(geneticInitialData));
        }
//        Population newPopulation = processGenetic(population);
        for (;;) {
            LOGGER.info("Population generation : " + population.getPopulationIteration());
            population.setLectureDescription(geneticInitialData.getLectureDescription());
            population = processGenetic(population, geneticInitialData);
            if((population.getBestGenotype().getHardFitnessScore() == 100)
                    || population.getGenotypePopulation().size() == 0
                    || counterSameFitnessScore == 1000 ){
                break;
            }else{
                if(globalFitnessScore.equals(population.getBestGenotype().getFitnessScore())){
                    counterSameFitnessScore++;
                }else{
                    globalFitnessScore = population.getBestGenotype().getFitnessScore();
                }
            }

//            population = genotypeService.mapHintNewPopulation(newPopulation);
            //end and present score
            LOGGER.info("Max fitness score for generation " + population.getBestGenotype().getFitnessScore());

        }
        LOGGER.info("Max fitness score : "+ population.getBestGenotype().getFitnessScore());
        LOGGER.info("Max hard fitness score : "+ population.getBestGenotype().getHardFitnessScore());
        LOGGER.info("Max soft fitness score : "+ population.getBestGenotype().getSoftFitnessScore());
        LOGGER.info("Generation end " + population.getPopulationIteration());
        LOGGER.info("Same fitness score " + counterSameFitnessScore);
        LOGGER.info("Best genotype " +  population.getBestGenotype());
        return population;

    }

    private Population processGenetic(Population population, GeneticInitialData geneticInitialData) {
        //fitness function
        population.setFitnessScore(0.0);
        //check fitness score of population
        fitnessService.fitPopulation(population);
        //selection by roulete
//        fitnessService.selectionRoulette(population);
                //crossover
        Population newPopulation = new Population();
        newPopulation.setBestGenotype(population.getBestGenotype());
        newPopulation.setPopulationIteration(population.getPopulationIteration()+ 1);
        for (int i = 0; i < population.getGenotypePopulation().size()/2; i++) {
//            Genotype genotypeFirst = fitnessService.getGenotypeBySelection(population);
            Genotype genotypeFirst = new Genotype(fitnessService.selectionTournament(population));
//            Genotype genotypeSecond = fitnessService.getGenotypeBySelection(population);
            Genotype genotypeSecond = new Genotype(fitnessService.selectionTournament(population));
            genotypeService.crossover(genotypeFirst, genotypeSecond, population.getLectureDescription().getNumberPerDay());
            newPopulation.getGenotypePopulation().add(genotypeFirst);
            newPopulation.getGenotypePopulation().add(genotypeSecond);
        }
        for (int i = 0; i < newPopulation.getGenotypePopulation().size(); i++) {
            genotypeService.mutateGenotype(newPopulation.getGenotypePopulation().get(i), geneticInitialData);
            genotypeService.mapHintGenotype(newPopulation.getGenotypePopulation().get(i));
        }
        return newPopulation;
    }
}
