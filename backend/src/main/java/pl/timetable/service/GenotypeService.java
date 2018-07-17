package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.timetable.dto.*;

import java.util.*;

@Service
public class GenotypeService {

    private HardGenotypeCriteria hardGenotypeCriteria;

    @Autowired
    public GenotypeService(@Qualifier("hardGenotypeCriteria") HardGenotypeCriteria hardGenotypeCriteria) {
        this.hardGenotypeCriteria = hardGenotypeCriteria;
    }

    public void setHardGenotypeCriteria(HardGenotypeCriteria hardGenotypeCriteria) {
        this.hardGenotypeCriteria = hardGenotypeCriteria;
    }

    private static final Logger LOGGER = Logger.getLogger(GenotypeService.class);
    public void mapHintGenotype(Genotype genotype){
        for (int i = 0; i < genotype.getGenotypeTable().length; i++) {
            for (int j = 0; j < genotype.getGenotypeTable()[i].length; j++) {
                Cell cell = genotype.getGenotypeTable()[i][j];
                if(Objects.nonNull(cell) && Objects.nonNull(cell.getRoomDto())){
                    genotype.getRoomByLecture().putIfAbsent(cell.getLecture(), new ArrayList<>());
                    genotype.getLectureByRoom().putIfAbsent(cell.getRoomDto(), new ArrayList<>());
                    genotype.getRoomByLecture().get(cell.getLecture()).add(cell.getRoomDto());
                    genotype.getLectureByRoom().get(cell.getRoomDto()).add(cell.getLecture());
                }
            }
        }

//
    }
    public Genotype createInitialGenotype(GeneticInitialData geneticInitialData) {

        int horizonSize = geneticInitialData.getLecture().getNumberPerDay() * geneticInitialData.getRoomDtoList().size() * geneticInitialData.getLecture().getDaysPerWeek();
        int lectureIncrementNumber = 1;
        int roomIncrementNumber = 0;
        Genotype genotype = new Genotype(geneticInitialData.getGroupDtoList().size(), horizonSize);
        genotype.getRoomByLecture().put(lectureIncrementNumber, new ArrayList<>());
        genotype.getLectureByRoom().put(geneticInitialData.getRoomDtoList().get(roomIncrementNumber), new ArrayList<>());
        Integer putRoomPosition = getRandomIntegerBetweenRange(0, geneticInitialData.getRoomDtoList().size());
        for (int i = 0; i < genotype.getGenotypeTable().length; i++) {
            Map<Integer, List<RoomDto>> roomByLectureTemporary = new HashMap<>();
            Map<RoomDto, List<Integer>> lectureByRoomTemporary = new HashMap<>();
            roomByLectureTemporary.put(1, new ArrayList<>());
            lectureByRoomTemporary.put(geneticInitialData.getRoomDtoList().get(0), new ArrayList<>());
            for (int j = 0; j < genotype.getGenotypeTable()[i].length; j++) {
                if (roomIncrementNumber == geneticInitialData.getRoomDtoList().size()) {
                    roomIncrementNumber = 0;
                    lectureIncrementNumber++;
                    putRoomPosition = getRandomIntegerBetweenRange(0, geneticInitialData.getRoomDtoList().size());
                    if (lectureIncrementNumber <= getLectureSlot(geneticInitialData)) {
                        genotype.getRoomByLecture().putIfAbsent(lectureIncrementNumber, new ArrayList<>());
                        roomByLectureTemporary.putIfAbsent(lectureIncrementNumber, new ArrayList<>());
                    }
                }
                if (lectureIncrementNumber == getLectureSlot(geneticInitialData) + 1) {
                    lectureIncrementNumber = 1;
                }
                genotype.getLectureByRoom().putIfAbsent(geneticInitialData.getRoomDtoList().get(roomIncrementNumber), new ArrayList<>());
                lectureByRoomTemporary.putIfAbsent(geneticInitialData.getRoomDtoList().get(roomIncrementNumber), new ArrayList<>());
//                Boolean roomBooked = new Random().nextBoolean();
                genotype.getGenotypeTable()[i][j] = new Cell(lectureIncrementNumber,
                        getRoomByRandomNumber(geneticInitialData, roomIncrementNumber, putRoomPosition),
                        null, null,
                        geneticInitialData.getGroupDtoList().get(i));
                if (Objects.nonNull(putRoomPosition) && putRoomPosition == roomIncrementNumber) {
                    lectureByRoomTemporary.get(geneticInitialData.getRoomDtoList().get(roomIncrementNumber)).add(lectureIncrementNumber);
                    roomByLectureTemporary.get(lectureIncrementNumber).add(geneticInitialData.getRoomDtoList().get(roomIncrementNumber));
//                    genotype.getLectureByRoom().get(geneticInitialData.getRoomDtoList().get(roomIncrementNumber)).add(lectureIncrementNumber);
//                    genotype.getRoomByLecture().get(lectureIncrementNumber).add(geneticInitialData.getRoomDtoList().get(roomIncrementNumber));
                }

                roomIncrementNumber++;
            }
            if (!genotype.getRoomByLecture().get(1).isEmpty()) {
                genotype.getRoomByLecture().forEach((integer, roomDtos) -> roomByLectureTemporary.get(integer).addAll(roomDtos));
            }
            if (hardGenotypeCriteria.checkRoomDuplicatesByLecture(roomByLectureTemporary)) {
                i++;
                genotype.setRoomByLecture(roomByLectureTemporary);
                genotype.getLectureByRoom().putAll(lectureByRoomTemporary);
            } else {
//                genotype.getGenotypeTable()[i] = new Cell[]{};
            }
        }
//        mapHintGenotype(genotype);
        LOGGER.info(genotype);
        return genotype;
    }

    public void mapHintNewPopulation(Population population){
        population.getGenotypePopulation().forEach(this::mapHintGenotype);
    }

    public void crossover(Genotype genotypeFirst, Genotype genotypeSecond){
        Integer rowSize = genotypeFirst.getGenotypeTable().length;
        Integer columnSize = genotypeFirst.getGenotypeTable()[0].length;
        Set<Integer> rowSet = new HashSet<>();
        Set<Integer> columnSet = new HashSet<>();
        for (int i = 0; i < rowSize; i++) {
            rowSet.add(new Random().ints(0, (rowSize)).findFirst().getAsInt());
        }
        for (int i = 0; i < columnSize; i++) {
            columnSet.add(new Random().ints(0, (columnSize)).findFirst().getAsInt());
        }
        rowSet.forEach(rowInteger -> {
            columnSet.forEach(columnInteger -> {
                Cell tempFirst = genotypeFirst.getGenotypeTable()[rowInteger][columnInteger];
                Cell tempSecond = genotypeSecond.getGenotypeTable()[rowInteger][columnInteger];
                genotypeFirst.getGenotypeTable()[rowInteger][columnInteger] = tempSecond;
                genotypeSecond.getGenotypeTable()[rowInteger][columnInteger] = tempFirst;
            });
        });
    }

    private RoomDto getRoomByRandomNumber(GeneticInitialData geneticInitialData, int roomIncrementNumber, Integer putRoomPosition) {
        if(Objects.isNull(putRoomPosition) || putRoomPosition != roomIncrementNumber){
            return null;
        }else{
            return geneticInitialData.getRoomDtoList().get(roomIncrementNumber);
        }
    }


    public static Integer getRandomIntegerBetweenRange(Integer min, Integer max) {
        Random random = new Random();
        Integer randomNumber = random.ints(min, (max + 1)).findFirst().getAsInt();
        if(Objects.equals(randomNumber, max)){
            return null;
        }else{
            return randomNumber;
        }
    }

    private static Integer getLectureSlot(GeneticInitialData geneticInitialData) {
        return geneticInitialData.getLecture().getNumberPerDay() * geneticInitialData.getLecture().getDaysPerWeek();
    }

}
