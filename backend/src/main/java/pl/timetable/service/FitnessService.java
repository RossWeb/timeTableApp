package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pl.timetable.dto.Genotype;
import pl.timetable.dto.Population;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FitnessService {

    private static final Logger LOGGER = Logger.getLogger(FitnessService.class);

    private HardGenotypeCriteria hardGenotypeCriteria;
    private SoftGenotypeCriteria softGenotypeCriteria;

    public FitnessService(HardGenotypeCriteria hardGenotypeCriteria,
                          SoftGenotypeCriteria softGenotypeCriteria) {
        this.hardGenotypeCriteria = hardGenotypeCriteria;
        this.softGenotypeCriteria = softGenotypeCriteria;
    }

    public void fitPopulation(Population population) {
        List<Genotype> genotypes = population.getGenotypePopulation().stream()
//                .filter(genotype -> hardGenotypeCriteria.checkData(genotype, population.getLectureDescriptionDto()))
                .collect(Collectors.toList());
        population.setBestGenotypeGeneration(null);
        genotypes.forEach(genotype -> {
            softGenotypeCriteria.checkData(genotype, population.getLectureDescriptionDto());
            hardGenotypeCriteria.checkData(genotype, population.getLectureDescriptionDto());
            genotype.setFitnessScore(genotype.getHardFitnessScore() + genotype.getSoftFitnessScore());
//            genotype.setFitnessScore(genotypes.size() / population.getGenotypePopulation().size() * 1.00);
            population.setFitnessScore(population.getFitnessScore() + genotype.getFitnessScore());
            if(Objects.nonNull(population.getBestGenotype()) && population.getBestGenotype().getFitnessScore() < genotype.getFitnessScore()) {
                population.setBestGenotype(genotype);
            }

            if(Objects.nonNull(population.getBestGenotypeGeneration()) && population.getBestGenotypeGeneration().getFitnessScore() < genotype.getFitnessScore()){
                population.setBestGenotypeGeneration(genotype);
            }
            if(Objects.isNull(population.getBestGenotypeGeneration())){
                population.setBestGenotypeGeneration(genotype);
            }

            if(Objects.isNull(population.getBestGenotype())){
                population.setBestGenotype(genotype);
            }


        });
        population.setGenotypePopulation(genotypes);
    }

    public Genotype selectionTournament(Population population){
        int firstGenotypePosition = new Random().ints(0, population.getGenotypePopulation().size()).findFirst().getAsInt();
        int secondGenotypePosition = new Random().ints(0, population.getGenotypePopulation().size()).findFirst().getAsInt();
        Genotype firstGenotype = population.getGenotypePopulation().get(firstGenotypePosition);
        Genotype secondGenotype = population.getGenotypePopulation().get(secondGenotypePosition);
        if(firstGenotype.getFitnessScore() >= secondGenotype.getFitnessScore()){
            return firstGenotype;
        }else {
            return secondGenotype;
        }
    }

    public void selectionRoulette(Population population) {
        Integer positionMin = 0;
        for (Genotype genotype : population.getGenotypePopulation()) {
            Double positionMax = genotype.getFitnessScore() / population.getFitnessScore() * 100.0;
            if(positionMin + positionMax > 100){
                break;
            }
            for (int i = positionMin; i < positionMin + positionMax; i++) {
                population.getSelectionArray()[i] = genotype;

            }
            positionMin += ((Long)Math.round(positionMax)).intValue();

        }
        if (Arrays.stream(population.getSelectionArray()).anyMatch(Objects::isNull)) {
            LOGGER.error("Array selection couldn't has none genotype");
        }
    }

    public Genotype getGenotypeBySelection(Population population) {
        return population.getSelectionArray()[new Random().ints(0, population.getSelectionArray().length
        ).findFirst().getAsInt()];
    }
}
