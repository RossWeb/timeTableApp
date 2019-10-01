package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.timetable.dto.*;
import pl.timetable.enums.TimeTableDescriptionStatus;
import pl.timetable.facade.TimeTableFacade;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class GeneticAlgorithmService {

    private static final Logger LOGGER = Logger.getLogger(GeneticAlgorithmService.class);

    private GenotypeService genotypeService;
    private FitnessService fitnessService;
    private TimeTableFacade timeTableFacade;
    private InitialGenotypeCriteria initialGenotypeCriteria;
    @Value("${sameFitnessScore}")
    private String sameFitnessScoreNumber = "20" ;

    public GeneticAlgorithmService(GenotypeService genotypeService, FitnessService fitnessService,
                                   TimeTableFacade timeTableFacade, InitialGenotypeCriteria initialGenotypeCriteria) {
        this.genotypeService = genotypeService;
        this.fitnessService = fitnessService;
        this.timeTableFacade = timeTableFacade;
        this.initialGenotypeCriteria = initialGenotypeCriteria;
    }

    public void setInitialGenotypeCriteria(InitialGenotypeCriteria initialGenotypeCriteria) {
        this.initialGenotypeCriteria = initialGenotypeCriteria;
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

    public Integer init(GeneticInitialData geneticInitialData) throws OperationNotSupportedException {

        boolean initalGenotypeCriteriaIsValid = initialGenotypeCriteria.checkData(geneticInitialData);
        if(!initalGenotypeCriteriaIsValid){
            throw new OperationNotSupportedException("GenotypeNotValid");
        }
        LectureDescriptionDto lectureDescriptionDto = geneticInitialData.getLectureDescriptionDto();
        TimeTableDescriptionDto timeTableDescriptionDto = timeTableFacade.saveTimeTableDescription("timeTable" + LocalDateTime.now(),
                LocalDateTime.now());
        lectureDescriptionDto.setTimeTableDescriptionId(timeTableDescriptionDto.getId());
        timeTableFacade.saveLectureDescription(lectureDescriptionDto);
        //generate population async
//        generatePopulation(geneticInitialData, timeTableDescriptionDto);
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
        long start = System.currentTimeMillis();
        Population population = new Population();
        population.setGeneticInitialData(geneticInitialData);
        Double globalFitnessScore = 0.0;
        Integer counterSameFitnessScore = 0;
//        Integer populationIteration = 0;
        for (int i = 0; i < geneticInitialData.getPopulationSize(); i++) {
            LOGGER.info("Create population : " + i);
            try {
                population.getGenotypePopulation().add(genotypeService.createInitialGenotype(geneticInitialData));
            }catch (Exception e){
                LOGGER.error(e);
            }
        }
//        WorkBookService.createWorkBookByGenotype(population.getGenotypePopulation().get(0));
//        Population newPopulation = processGenetic(population);
        for (; ; ) {
            population.setLectureDescriptionDto(geneticInitialData.getLectureDescriptionDto());
            try {
                population = processGenetic(population, geneticInitialData);
            } catch (CloneNotSupportedException e) {
                LOGGER.error("Error generating timetable");
            }
            LOGGER.info("Population generation : " + population.getPopulationIteration());
            if ((population.getBestGenotype().getHardFitnessScore() == 100 && population.getBestGenotype().getFitnessScore() >= 200)
                    || population.getGenotypePopulation().size() == 0
                    || counterSameFitnessScore.equals(Integer.valueOf(sameFitnessScoreNumber))) {
                break;
            } else {
                addGenotypeReport(timeTableDescriptionDto, population);
                if (globalFitnessScore.equals(population.getBestGenotype().getFitnessScore())) {
                    counterSameFitnessScore++;
                } else {
                    counterSameFitnessScore = 0;
                    globalFitnessScore = population.getBestGenotype().getFitnessScore();
                }
            }

//            population = genotypeService.mapHintNewPopulation(newPopulation);
            //end and present score
            LOGGER.info("Max fitness score for generation " + population.getBestGenotype().getFitnessScore());

        }
        LOGGER.info("Algorithm duration " + (System.currentTimeMillis() - start) + "ms");
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

    private Population processGenetic(Population population, GeneticInitialData geneticInitialData) throws CloneNotSupportedException {
        Integer populationSize = population.getGenotypePopulation().size();
        //fitness function
        population.setFitnessScore(0.0);
        //check fitness score of population
        fitnessService.fitPopulation(population);
        List<Genotype> genotypes = population.getGenotypePopulation().stream().filter(genotype -> genotype.getHardFitnessScore() > 80.0).collect(Collectors.toList());
        Integer genotypeSize = populationSize - genotypes.size();
        for (int i = 0; i < genotypeSize; i++) {
            genotypes.add(genotypeService.createInitialGenotype(population.getGeneticInitialData()));
        }
        population.setGenotypePopulation(genotypes);
        fitnessService.fitPopulation(population);
        //selection by roulete
//        fitnessService.selectionRoulette(population);
        //crossover
        Population newPopulation = new Population();
        newPopulation.setBestGenotype(population.getBestGenotype());
        newPopulation.setBestGenotypeGeneration(population.getBestGenotypeGeneration());
        newPopulation.setGeneticInitialData(population.getGeneticInitialData());
        newPopulation.setPopulationIteration(population.getPopulationIteration() + 1);
        for (int i = 0; i < populationSize ; i++) {
//            Genotype genotypeFirst = fitnessService.getGenotypeBySelection(population);
            Genotype genotypeFirst = new Genotype(fitnessService.selectionTournament(population));
//            Genotype genotypeSecond = fitnessService.getGenotypeBySelection(population);
            Genotype genotypeSecond = new Genotype(fitnessService.selectionTournament(population));
//            genotypeService.crossover(genotypeFirst, genotypeSecond, population.getLectureDescriptionDto().getNumberPerDay());
//            genotypeService.crossoverPlus(genotypeFirst, genotypeSecond, population.getLectureDescriptionDto().getNumberPerDay());
//            newPopulation.getGenotypePopulation().add(genotypeService.crossoverChild(genotypeFirst, genotypeSecond));
            Genotype genotype = genotypeService.crossoverNew(genotypeFirst, genotypeSecond);
            newPopulation.getGenotypePopulation().add(genotype);
//            newPopulation.getGenotypePopulation().add(genotypeFirst);
//            newPopulation.getGenotypePopulation().add(genotypeSecond);
            if(Arrays.stream(newPopulation.getGenotypePopulation().get(0).getGenotypeTable()[0]).filter(room -> Objects.nonNull(room) && Objects.isNull(room.getRoomDto())).count() != 2 ||
                    Arrays.stream(newPopulation.getGenotypePopulation().get(0).getGenotypeTable()[1]).filter(room -> Objects.nonNull(room) && Objects.isNull(room.getRoomDto())).count() != 2 ||
                    Arrays.stream(newPopulation.getGenotypePopulation().get(0).getGenotypeTable()[2]).filter(room -> Objects.nonNull(room) && Objects.isNull(room.getRoomDto())).count() != 2  ||
                    Arrays.stream(newPopulation.getGenotypePopulation().get(0).getGenotypeTable()[3]).filter(room -> Objects.nonNull(room) && Objects.isNull(room.getRoomDto())).count() != 2 ){
                LOGGER.info("Dupa");
            }
        }
        for (int i = 0; i < newPopulation.getGenotypePopulation().size(); i++) {

            genotypeService.mutateGenotype(newPopulation.getGenotypePopulation().get(i), geneticInitialData);
            genotypeService.mapHintGenotype(newPopulation.getGenotypePopulation().get(i));

        }

        return newPopulation;
    }
}
