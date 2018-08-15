package pl.timetable.service;

import org.springframework.stereotype.Service;
import pl.timetable.dto.Cell;
import pl.timetable.dto.Genotype;
import pl.timetable.dto.LectureDescription;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service(value = "softGenotypeCriteria")
public class SoftGenotypeCriteria implements AbstractCriteria {

    @Override
    public boolean checkData(Genotype genotype, LectureDescription lectureDescription) {
        genotype.setSoftFitnessScore(
                (addBonusForMorningLecture(genotype, lectureDescription) +
                        addBonusForLecturePerAnyDayPerWeekForSemester(genotype, lectureDescription)) * 100);

        return true;
    }

    public Double addBonusForMorningLecture(Genotype genotype, LectureDescription lectureDescription){
        int bonusScoreDefinition = lectureDescription.getNumberPerDay() * lectureDescription.getNumberPerDay();
        int daysSize = lectureDescription.getWeeksPerSemester() * lectureDescription.getDaysPerWeek();
        int maxBonus = bonusScoreDefinition * daysSize;
        int groupSize = genotype.getGenotypeTable().length;
        int score = Arrays.stream(genotype.getGenotypeTable()).mapToInt(group -> {
            int bonusScore = 0;
            for (int i = 0; i < daysSize ; i++) {
                int searchedDay = i +1;
                List<Cell> cellsPerDay = Arrays.stream(group).filter(cell -> cell.getDay().equals(searchedDay)).collect(Collectors.toList());
                for (int j = 0; j < cellsPerDay.size(); j++) {
                    Cell cell = cellsPerDay.get(j);
                    if(Objects.nonNull(cell.getRoomDto()) && Objects.nonNull(cell.getSubjectDto())){
                        bonusScore += bonusScoreDefinition - j*lectureDescription.getNumberPerDay();
                        break;
                    }

                }
            }
            return bonusScore;
        }).sum();
        return calculateScorePerCriteria(score / groupSize, maxBonus);

    }

    public Double addBonusForLecturePerAnyDayPerWeekForSemester(Genotype genotype, LectureDescription lectureDescription){
        int maxBonus = lectureDescription.getWeeksPerSemester() * lectureDescription.getDaysPerWeek();
        int groupSize = genotype.getGenotypeTable().length;
        int score = Arrays.stream(genotype.getGenotypeTable()).mapToInt(group -> {
            int bonusScore = 0;
            for (int i = 0; i < group.length/lectureDescription.getNumberPerDay() ; i++) {
                int searchedDay = i +1;
                List<Cell> cellsPerDay = Arrays.stream(group).filter(cell -> cell.getDay().equals(searchedDay)).collect(Collectors.toList());
                boolean lectureExisted = cellsPerDay.stream().anyMatch(cell -> Objects.nonNull(cell.getRoomDto()) && Objects.nonNull(cell.getSubjectDto()));
                if(lectureExisted){
                   bonusScore++;
                }
            }
            return bonusScore;
        }).sum();
        return calculateScorePerCriteria(score /groupSize , maxBonus);
    }

    private static Double calculateScorePerCriteria(Integer score, Integer maxBonus){
        return score / maxBonus.doubleValue() * 0.50;
    }


}


