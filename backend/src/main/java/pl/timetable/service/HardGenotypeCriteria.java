package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pl.timetable.dto.*;
import pl.timetable.entity.Subject;

import java.util.*;
import java.util.stream.Collectors;


/*
    1. Każda klasa nie może realizować zajęć w więcej niż jeden sali w tym samym czasie.
    2. Każdy nauczyciel może realizować zajęcia tylko w jednej sali w tym samym czasie.
    3. Każda sala może być przypisana do jednego przedmiotu w danym momencie.
    4. Dwie klasy nie mogą realizować zajęć w tej samej sali w tym samym czasie
 */
@Service(value = "hardGenotypeCriteria")
public class HardGenotypeCriteria implements AbstractCriteria {

    private static final Logger LOGGER = Logger.getLogger(HardGenotypeCriteria.class);

    @Override
    public boolean checkData(Genotype genotype, LectureDescriptionDto lectureDescriptionDto) {
        Boolean isValid = true;
        int groupSize = genotype.getGenotypeTable().length;
        double hardFitnessScoreByGroupDefinition = 0.33;
        double hardFitnessScoreByGroup = 0.0;
        for (Cell[] group: genotype.getGenotypeTable()) {
            Integer subjectSize = group[0].getCourseDto().getSubjectSet().stream().map(Subject::getSize).mapToInt(value -> value).sum();
            Integer lectureSize = ((Long) Arrays.stream(group).filter(cell -> Objects.nonNull(cell) && Objects.nonNull(cell.getRoomDto())).count()).intValue();
            boolean isEnoughSizeLecture = hasEnoughSizeLectureToSubject(lectureSize, subjectSize);
            if(isEnoughSizeLecture){
                hardFitnessScoreByGroup += 1.0 / groupSize;
            }
        }

        genotype.setHardFitnessScore(hardFitnessScoreByGroup * (hardFitnessScoreByGroupDefinition + 0.01));
        isValid = hasNoRoomDuplicatesByLecture(genotype.getRoomByLecture()) && hasNoGroupDuplicatesByRoom(genotype.getGenotypeTable());
        if(isValid){
            genotype.setHardFitnessScore(genotype.getHardFitnessScore() + hardFitnessScoreByGroupDefinition);
        }
        isValid = hasNoTeacherPerLectureDuplicates(genotype.getLectureByTeacher());
        if(isValid) {
            genotype.setHardFitnessScore(genotype.getHardFitnessScore() + hardFitnessScoreByGroupDefinition);
        }
        genotype.setHardFitnessScore(genotype.getHardFitnessScore()*100);
        return isValid;
    }

    public boolean hasNoTeacherPerLectureDuplicates(Map<Integer,  List<Integer>> lectureByTeacher){
        boolean isValid = true;
        for (List<Integer> lectures : lectureByTeacher.values()) {
            Set<Integer> lecturesSet = new HashSet<>(lectures);
            if (lecturesSet.size() != lectures.size()) {
                isValid = false;
                LOGGER.error("Teacher has two lecture at the same time");
                break;
            }
        }
        return isValid;
    }

    public boolean hasNoRoomDuplicatesByLecture(Map<Integer, LinkedList<RoomDto>> roomByLecture) {
        Set<RoomDto> roomDtoSet = new HashSet<>();
        roomByLecture.forEach((integer, roomDtos) -> {
            roomDtoSet.addAll(findDuplicates(roomDtos));

        });
        boolean isValid = roomDtoSet.isEmpty();
        if(!isValid){
            LOGGER.info("Duplicates count : " + roomDtoSet.size());
        }
        return isValid;
    }

    public boolean hasEnoughSizeLectureToSubject(Integer lectureSize, Integer subjectSize){
        boolean isValid = lectureSize >= subjectSize;
        if(!isValid){
            LOGGER.error("Group has no space to subject. Lecture : " + lectureSize + " subject : " + subjectSize);
        }
        return isValid;
    }

    public boolean hasNoGroupDuplicatesByRoom(Cell[][] genotable){
        boolean isValid = true;
        int lectureSize = genotable[0].length;
        for (int i = 0; i < lectureSize ; i++) {
            Set<RoomDto> roomDtoSet = new HashSet<>();
            for (Cell[] aGenotable : genotable) {
                Cell cell = aGenotable[i];
                if (Objects.nonNull(cell) && Objects.nonNull(cell.getRoomDto())) {
                    RoomDto temporaryRoom = aGenotable[i].getRoomDto();
                    if (roomDtoSet.contains(temporaryRoom)) {
                        LOGGER.error("Two group has same room " + temporaryRoom + " for lecture " + i);
                        isValid = false;
                    } else {
                        roomDtoSet.add(temporaryRoom);
                    }
                }
            }
        }

        return isValid;
    }

    private <T> Set<T> findDuplicates(Collection<T> collection) {
        Set<T> uniques = new HashSet<>();
        return collection.stream()
                .filter(e -> Objects.nonNull(e) && !uniques.add(e))
                .collect(Collectors.toSet());
    }

}
