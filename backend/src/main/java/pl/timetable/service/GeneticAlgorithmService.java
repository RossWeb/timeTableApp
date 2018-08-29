package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.timetable.dto.*;
import pl.timetable.enums.TimeTableDescriptionStatus;
import pl.timetable.facade.TimeTableFacade;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class GeneticAlgorithmService {

    private static final Logger LOGGER = Logger.getLogger(GeneticAlgorithmService.class);

    private GenotypeService genotypeService;
    private FitnessService fitnessService;
    private TimeTableFacade timeTableFacade;

    public GeneticAlgorithmService(GenotypeService genotypeService, FitnessService fitnessService, TimeTableFacade timeTableFacade) {
        this.genotypeService = genotypeService;
        this.fitnessService = fitnessService;
        this.timeTableFacade = timeTableFacade;
    }

    public void setFitnessService(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
    }

    public void setGenotypeService(GenotypeService genotypeService) {
        this.genotypeService = genotypeService;
    }

    public void setTimeTableFacade(TimeTableFacade timeTableFacade) {
        this.timeTableFacade = timeTableFacade;
    }

    public Integer init(GeneticInitialData geneticInitialData) {

        LectureDescriptionDto lectureDescriptionDto = geneticInitialData.getLectureDescriptionDto();
        TimeTableDescriptionDto timeTableDescriptionDto = timeTableFacade.saveTimeTableDescription("timeTable" + LocalDateTime.now(),
                LocalDateTime.now());
        lectureDescriptionDto.setTimeTableDescriptionId(timeTableDescriptionDto.getId());
        timeTableFacade.saveLectureDescription(lectureDescriptionDto);
        //generate population async
        CompletableFuture.supplyAsync(() -> generatePopulation(geneticInitialData, timeTableDescriptionDto)).whenComplete((population, throwable) -> {
            if (Objects.nonNull(throwable)) {
                changeTimeTableDescriptionStatus(timeTableDescriptionDto, TimeTableDescriptionStatus.ERROR);
                LOGGER.error("Error when async generic genotype. " + throwable);
            }

            if (Objects.nonNull(population)) {
                changeTimeTableDescriptionStatus(timeTableDescriptionDto, TimeTableDescriptionStatus.SUCCESS);
            } else {
                changeTimeTableDescriptionStatus(timeTableDescriptionDto, TimeTableDescriptionStatus.ERROR);
                LOGGER.error("No population. Genetic algorithm couldnt completed");
            }
        });
        return timeTableDescriptionDto.getId();

    }

    private void changeTimeTableDescriptionStatus(TimeTableDescriptionDto timeTableDescriptionDto, TimeTableDescriptionStatus timeTableDescriptionStatus) {
        boolean changed = timeTableFacade.changeTimeTableDescriptionStatus(timeTableDescriptionDto.getId(), timeTableDescriptionStatus);
        if (!changed) {
            LOGGER.error("Couldnt change timeTableDescription status for id " + timeTableDescriptionDto.getId());
        }
    }

    @Async
    private Population generatePopulation(GeneticInitialData geneticInitialData, TimeTableDescriptionDto timeTableDescriptionDto) {
        Population population = new Population();
        Double globalFitnessScore = 0.0;
        Integer counterSameFitnessScore = 0;
        Integer sameFitnessScoreNumber = 100;
//        Integer populationIteration = 0;
        for (int i = 0; i < geneticInitialData.getPopulationSize(); i++) {
            LOGGER.info("Create population : " + i);
            try {
                population.getGenotypePopulation().add(genotypeService.createInitialGenotype(geneticInitialData));
            }catch (Exception e){
                LOGGER.error(e);
            }
        }
//        Population newPopulation = processGenetic(population);
        for (; ; ) {
            population.setLectureDescriptionDto(geneticInitialData.getLectureDescriptionDto());
            population = processGenetic(population, geneticInitialData);
            LOGGER.info("Population generation : " + population.getPopulationIteration());
            if ((population.getBestGenotype().getHardFitnessScore() == 100 && population.getBestGenotype().getFitnessScore() >= 200)
                    || population.getGenotypePopulation().size() == 0
                    || counterSameFitnessScore.equals(sameFitnessScoreNumber)) {
                break;
            } else {
                addGenotypeReport(timeTableDescriptionDto, population);
                if (globalFitnessScore.equals(population.getBestGenotype().getFitnessScore())) {
                    counterSameFitnessScore++;
                } else {
                    globalFitnessScore = population.getBestGenotype().getFitnessScore();
                }
            }

//            population = genotypeService.mapHintNewPopulation(newPopulation);
            //end and present score
            LOGGER.info("Max fitness score for generation " + population.getBestGenotype().getFitnessScore());

        }
        LOGGER.info("Max fitness score : " + population.getBestGenotype().getFitnessScore());
        LOGGER.info("Max hard fitness score : " + population.getBestGenotype().getHardFitnessScore());
        LOGGER.info("Max soft fitness score : " + population.getBestGenotype().getSoftFitnessScore());
        LOGGER.info("Generation end " + population.getPopulationIteration());
        LOGGER.info("Same fitness score " + counterSameFitnessScore);
        LOGGER.info("Best genotype " + population.getBestGenotype());
        timeTableFacade.saveGenotype(population.getBestGenotype(), timeTableDescriptionDto);
        addGenotypeReport(timeTableDescriptionDto, population);
        return population;
    }

    private void addGenotypeReport(TimeTableDescriptionDto timeTableDescriptionDto, Population population) {
        ReportPopulationDto reportPopulationDto = new ReportPopulationDto();
        reportPopulationDto.setPopulationSize(population.getGenotypePopulation().size());
        reportPopulationDto.setTimeTableDescriptionId(timeTableDescriptionDto.getId());
        reportPopulationDto.setPopulationGeneration(population.getPopulationIteration());
        reportPopulationDto.setBestSoftFitnessScore(population.getBestGenotypeGeneration().getSoftFitnessScore());
        reportPopulationDto.setBestHardFitnessScore(population.getBestGenotypeGeneration().getHardFitnessScore());
        reportPopulationDto.setBestFitnessScore(population.getBestGenotypeGeneration().getFitnessScore());
        timeTableFacade.addReportGenotype(reportPopulationDto);
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
        newPopulation.setBestGenotypeGeneration(population.getBestGenotypeGeneration());
        newPopulation.setPopulationIteration(population.getPopulationIteration() + 1);
        for (int i = 0; i < population.getGenotypePopulation().size() / 2; i++) {
//            Genotype genotypeFirst = fitnessService.getGenotypeBySelection(population);
            Genotype genotypeFirst = new Genotype(fitnessService.selectionTournament(population));
//            Genotype genotypeSecond = fitnessService.getGenotypeBySelection(population);
            Genotype genotypeSecond = new Genotype(fitnessService.selectionTournament(population));
            genotypeService.crossover(genotypeFirst, genotypeSecond, population.getLectureDescriptionDto().getNumberPerDay());
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
