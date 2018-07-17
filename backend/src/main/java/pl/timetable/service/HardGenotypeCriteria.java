package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pl.timetable.dto.Cell;
import pl.timetable.dto.Genotype;
import pl.timetable.dto.RoomDto;

import java.util.*;
import java.util.stream.Collectors;


/*
    1. Każda klasa nie może realizować zajęć w więcej niż jeden sali w tym samym czasie.
    2. Zajęcia powinny być realizowane bez okienek
    3. Każdy nauczyciel może realizować zajęcia tylko w jednej sali w tym samym czasie.
    4. Każda sala może być przypisana do jednego przedmiotu w danym momencie.
    5. Dwie klasy nie mogą realizować zajęć w tej samej sali w tym samym czasie
 */
@Service(value = "hardGenotypeCriteria")
public class HardGenotypeCriteria implements AbstractCriteria {

    private static final Logger LOGGER = Logger.getLogger(HardGenotypeCriteria.class);

    @Override
    public boolean checkData(Genotype genotype) {
        Boolean isValid = true;
//        Cell[] row = genotype.getGenotypeTable()[genotype.getGenotypeTable().length];
        isValid = checkRoomDuplicatesByLecture(genotype.getRoomByLecture());
        return isValid;
    }

    public boolean checkRoomDuplicatesByLecture(Map<Integer, List<RoomDto>> roomByLecture) {
        Set<RoomDto> roomDtoSet = new HashSet<>();
        roomByLecture.forEach((integer, roomDtos) -> {
            roomDtoSet.addAll(findDuplicates(roomDtos));

        });
//        LOGGER.info("Duplicates count : "+ roomDtoSet.size());
        return roomDtoSet.isEmpty();
    }

    private <T> Set<T> findDuplicates(Collection<T> collection) {
        Set<T> uniques = new HashSet<>();
        return collection.stream()
                .filter(e -> !uniques.add(e))
                .collect(Collectors.toSet());
    }
}
