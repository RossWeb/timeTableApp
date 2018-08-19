package pl.timetable.service;

import org.apache.log4j.Logger;
import pl.timetable.dto.GeneticInitialData;

public class InitialGenotypeCriteria {

    private static final Logger LOGGER = Logger.getLogger(InitialGenotypeCriteria.class);


    public boolean checkData(GeneticInitialData geneticInitialData) {
        boolean isValid = true;
        isValid = checkEnoughRoomAndLectureToSubjectPerSemester(geneticInitialData);
        return isValid;
    }

    private boolean checkEnoughRoomAndLectureToSubjectPerSemester(GeneticInitialData geneticInitialData) {
        LOGGER.info("Checking enough room and lecture to subject per semester");
        Integer roomSize = geneticInitialData.getRoomDtoList().size();
        Integer lectureSizePerWeek = geneticInitialData.getLectureDescriptionDto().getNumberPerDay() * geneticInitialData.getLectureDescriptionDto().getDaysPerWeek();
        Integer spaceSizePerSemester = roomSize * lectureSizePerWeek * geneticInitialData.getLectureDescriptionDto().getWeeksPerSemester();
        Integer subjectSizePerSemester = ((Long) geneticInitialData.getGroupDtoList().stream().map(groupDto -> groupDto.getCourse().getSubjectSet().size()).count()).intValue();
        boolean enough = spaceSizePerSemester >= subjectSizePerSemester;
        if (!enough) {
            LOGGER.error("Number room and lecture isn't enough to subject size");
        }

        return enough;
    }

    //sprawdzenie groupy i zajecia razy sale
}


