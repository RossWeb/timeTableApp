package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.dto.LectureDescriptionDto;
import pl.timetable.dto.TimeTableDescriptionDto;
import pl.timetable.entity.LectureDescription;
import pl.timetable.entity.TimeTableDescription;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.LectureDescriptionRepository;
import pl.timetable.repository.TimeTableDescriptionRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class LectureDescriptionService {

    public static final Logger LOGGER = Logger.getLogger(LectureDescriptionService.class);
    private LectureDescriptionRepository lectureDescriptionRepository;
    private TimeTableDescriptionService timeTableDescriptionService;

    public void setLectureDescriptionRepository(LectureDescriptionRepository lectureDescriptionRepository) {
        this.lectureDescriptionRepository = lectureDescriptionRepository;
    }

    public LectureDescriptionService(LectureDescriptionRepository lectureDescriptionRepository, TimeTableDescriptionService timeTableDescriptionService) {
        this.lectureDescriptionRepository = lectureDescriptionRepository;
        this.timeTableDescriptionService = timeTableDescriptionService;
    }

    public void create(LectureDescriptionDto lectureDescriptionDto){
        LectureDescription lectureDescription = new LectureDescription();
        if(Objects.nonNull(lectureDescriptionDto.getTimeTableDescriptionId())){
            try {
                lectureDescription.setTimeTableDescription(
                        TimeTableDescriptionService.mapDtoToEntity(
                                timeTableDescriptionService.get(lectureDescriptionDto.getTimeTableDescriptionId())));
            } catch (EntityNotFoundException e) {
                LOGGER.info("Couldnt add timetableDescription to lectureDescription, not found timetableDescription");
            }
        }
        lectureDescription.setWeeksPerSemester(lectureDescriptionDto.getWeeksPerSemester());
        lectureDescription.setNumberPerDay(lectureDescriptionDto.getNumberPerDay());
        lectureDescription.setDaysPerWeek(lectureDescriptionDto.getDaysPerWeek());
        lectureDescriptionRepository.create(lectureDescription);
    }

    public LectureDescriptionDto getLectureDescriptionByTimeTableDescription(TimeTableDescriptionDto timeTableDescriptionDto) throws EntityNotFoundException {
        timeTableDescriptionService.get(timeTableDescriptionDto.getId());
        LectureDescription lectureDescription = lectureDescriptionRepository.getByTimeTableDescription(TimeTableDescriptionService.mapDtoToEntity(timeTableDescriptionDto));
        return mapEntityToDto(lectureDescription);
    }

    private static LectureDescriptionDto mapEntityToDto(LectureDescription lectureDescription){
        LectureDescriptionDto lectureDescriptionDto = new LectureDescriptionDto(lectureDescription.getNumberPerDay(), lectureDescription.getDaysPerWeek(), lectureDescription.getWeeksPerSemester());
        lectureDescriptionDto.setTimeTableDescriptionId(lectureDescription.getTimeTableDescription().getId());
        return lectureDescriptionDto;
    }

}
