package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pl.timetable.dto.*;

import java.util.*;
import java.util.stream.Collectors;

@Service(value = "softGenotypeCriteria")
public class SoftGenotypeCriteria implements AbstractCriteria {

    private static final Logger LOGGER = Logger.getLogger(SoftGenotypeCriteria.class);

    @Override
    public boolean checkData(Genotype genotype, LectureDescriptionDto lectureDescriptionDto) {
        genotype.setSoftFitnessScore(
                (addBonusForMorningLecture(genotype, lectureDescriptionDto) +
                        addBonusForLecturePerAnyDayPerWeekForSemester(genotype, lectureDescriptionDto) +
                hasNoEmptyLectureByGroup(genotype.getRoomByGroup(), lectureDescriptionDto.getNumberPerDay())) * 100);

        return true;
    }



    public Double addBonusForMorningLecture(Genotype genotype, LectureDescriptionDto lectureDescriptionDto){
        int bonusScoreDefinition = lectureDescriptionDto.getNumberPerDay() * lectureDescriptionDto.getNumberPerDay();
        int daysSize = lectureDescriptionDto.getWeeksPerSemester() * lectureDescriptionDto.getDaysPerWeek();
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
                        bonusScore += bonusScoreDefinition - j* lectureDescriptionDto.getNumberPerDay();
                        break;
                    }

                }
            }
            return bonusScore;
        }).sum();
        return calculateScorePerCriteria(score / groupSize, maxBonus);

    }



    public Double addBonusForLecturePerAnyDayPerWeekForSemester(Genotype genotype, LectureDescriptionDto lectureDescriptionDto){
        int maxBonus = lectureDescriptionDto.getWeeksPerSemester() * lectureDescriptionDto.getDaysPerWeek();
        int groupSize = genotype.getGenotypeTable().length;
        int score = Arrays.stream(genotype.getGenotypeTable()).mapToInt(group -> {
            int bonusScore = 0;
            for (int i = 0; i < group.length/ lectureDescriptionDto.getNumberPerDay() ; i++) {
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

    public Double hasNoEmptyLectureByGroup(Map<GroupDto, LinkedList<RoomDto>> roomByGroup , Integer lecturePerDay) {
        List<GroupDto> groupList = new ArrayList<>(roomByGroup.keySet());
        Boolean isValid = true;
        Integer emptyRooms = 0;
        Integer roomsSize = 0;
        for (GroupDto groupDto : groupList) {
            Integer lectureCounter = 0;
            List<RoomDto> roomDtoList = roomByGroup.get(groupDto);
            roomsSize += roomDtoList.size();
            if(roomDtoList.isEmpty()){
                return 0.0;
            }
            Boolean firstRoomExisted = false;
            Boolean lastEmptyRoomExisted = false;
            for (int i = 0; i < roomDtoList.size(); i++) {
                RoomDto roomDto = roomDtoList.get(i);
                if (Objects.nonNull(roomDto) && !firstRoomExisted) {
                    firstRoomExisted = true;
                } else if (Objects.isNull(roomDto) && firstRoomExisted ) {
                    lastEmptyRoomExisted = true;
                } else if (Objects.nonNull(roomDto) && lastEmptyRoomExisted) {
                    LOGGER.error("Group has empty room in the middle criterion not valid : " + roomDtoList + i);
                    isValid = false;
                    emptyRooms++;
                }
                lectureCounter++;
                if(lectureCounter.equals(lecturePerDay)){
                    firstRoomExisted = false;
                    lastEmptyRoomExisted = false;
                    lectureCounter = 0;
                }
            }

        }
        return (1 - emptyRooms / roomsSize.doubleValue()) * 0.34;
    }

    private static Double calculateScorePerCriteria(Integer score, Integer maxBonus){
        return score / maxBonus.doubleValue() * 0.33;
    }

}


