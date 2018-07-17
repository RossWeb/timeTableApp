package pl.timetable.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.timetable.dto.Genotype;
import pl.timetable.dto.Population;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static pl.timetable.service.GenotypeService.getRandomIntegerBetweenRange;

@Service
public class FitnessService {

    private HardGenotypeCriteria hardGenotypeCriteria;

    public FitnessService(HardGenotypeCriteria hardGenotypeCriteria) {
        this.hardGenotypeCriteria = hardGenotypeCriteria;
    }

    public void fitPopulation(Population population){
        List<Genotype> genotypes = population.getGenotypePopulation().stream().filter(genotype -> hardGenotypeCriteria.checkData(genotype)).collect(Collectors.toList());
        genotypes.forEach(genotype -> {
            genotype.setFitnessScore(genotypes.size() / population.getGenotypePopulation().size() * 1.00);
            population.setFitnessScore(population.getFitnessScore() + genotype.getFitnessScore());
        });
        population.setGenotypePopulation(genotypes);
    }

    public void selectionRoulette(Population population){
        Integer positionMin = 0;
        for (Genotype genotype: population.getGenotypePopulation()) {
            Double positionMax = genotype.getFitnessScore() / population.getFitnessScore() *100.0;
            for (int i = positionMin; i < positionMin + positionMax; i++) {
                population.getSelectionArray()[i] = genotype;

            }
            positionMin += positionMax.intValue();
        }
    }

    public Genotype getGenotypeBySelection(Population population){
        return population.getSelectionArray()[new Random().ints(0, (99 + 1)).findFirst().getAsInt()];
    }
}
