package pl.timetable.facade;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.TimeTableRequest;
import pl.timetable.dto.*;
import pl.timetable.entity.ReportPopulation;
import pl.timetable.entity.TimeTableDescription;
import pl.timetable.enums.TimeTableDescriptionStatus;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.service.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TimeTableFacade {

    public static final Logger LOGGER = Logger.getLogger(TimeTableFacade.class);

    private SessionFactory sessionFactory;
    private TimeTableService timeTableService;
    private LectureDescriptionService lectureDescriptionService;
    private TimeTableDescriptionService timeTableDescriptionService;
    private ReportPopulationService reportPopulationService;
    private GroupServiceImpl groupService;
    private RoomServiceImpl roomService;
    private SubjectServiceImpl subjectService;
    private CourseServiceImpl courseService;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public TimeTableFacade(TimeTableService timeTableService, LectureDescriptionService lectureDescriptionService, TimeTableDescriptionService timeTableDescriptionService, ReportPopulationService reportPopulationService, GroupServiceImpl groupService, RoomServiceImpl roomService, SubjectServiceImpl subjectService, CourseServiceImpl courseService) {
        this.timeTableService = timeTableService;
        this.lectureDescriptionService = lectureDescriptionService;
        this.timeTableDescriptionService = timeTableDescriptionService;
        this.reportPopulationService = reportPopulationService;
        this.groupService = groupService;
        this.roomService = roomService;
        this.subjectService = subjectService;
        this.courseService = courseService;
    }

    public void saveGenotype(Genotype genotype, TimeTableDescriptionDto timeTableDescription) {
        Arrays.stream(genotype.getGenotypeTable()).forEach(group -> {
            try {
                saveGroup(group, timeTableDescription);
            } catch (EntityNotFoundException e) {
                LOGGER.error("Couldnt save genotype");
            }
        });
    }

    public void addReportGenotype(ReportPopulationDto reportPopulationDto){
        try {
            reportPopulationService.create(reportPopulationDto);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Could not add report for reportPopulationDto" + reportPopulationDto + " timeTableDescription not found");
        }
    }

    public List<ReportPopulationDto> getReportPopulation(Integer timeTableDescriptionId){
        return reportPopulationService.getByTimeTableDescription(timeTableDescriptionId);
    }

    public TimeTableDescriptionDto saveTimeTableDescription(String name, LocalDateTime startedDate) {
        return TimeTableDescriptionService.mapEntityToDto(timeTableDescriptionService.create(name, startedDate));
    }

    public TimeTableDescriptionStatus getTimeTableDescriptionStatus(Integer timeTableDescriptionId){
        try {
            return timeTableDescriptionService.get(timeTableDescriptionId).getStatus();
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public boolean changeTimeTableDescriptionStatus(Integer timeTableDescriptionId, TimeTableDescriptionStatus status){
        try {
            timeTableDescriptionService.updateStatus(timeTableDescriptionId, status);
        } catch (EntityNotFoundException e) {
            return false;
        }
        return true;
    }

    public void saveLectureDescription(LectureDescriptionDto lectureDescriptionDto) {
        lectureDescriptionService.create(lectureDescriptionDto);
    }

    public GeneticInitialData getGeneticInitialData(TimeTableRequest timeTableRequest){
        GeneticInitialData geneticInitialData = new GeneticInitialData();
        geneticInitialData.setMutationValue(timeTableRequest.getMutationValue());
        geneticInitialData.setCourseDtoList(courseService.findAll());
        geneticInitialData.setPopulationSize(timeTableRequest.getPopulationSize());
        geneticInitialData.setSubjectDtoList(subjectService.findAll());
        geneticInitialData.setGroupDtoList(groupService.findAll());
        geneticInitialData.setRoomDtoList(roomService.findAll());
        LectureDescriptionDto lectureDescriptionDto =
                new LectureDescriptionDto(timeTableRequest.getNumberPerDay(), timeTableRequest.getDaysPerWeek(), timeTableRequest.getWeeksPerSemester());
        geneticInitialData.setLectureDescriptionDto(lectureDescriptionDto);
        return geneticInitialData;
    }

    public LectureDescriptionDto getLectureDescriptionByTimeTableDescription(TimeTableDescriptionDto timeTableDescriptionDto) {
        try {
            return lectureDescriptionService.getLectureDescriptionByTimeTableDescription(timeTableDescriptionDto);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Couldnt get any lecture description by timetableId : " + timeTableDescriptionDto.getId());
            return null;
        }
    }

    private void saveGroup(Cell[] cells, TimeTableDescriptionDto timeTableDescriptionDto) throws EntityNotFoundException {
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            TimeTableDto timeTable = new TimeTableDto();
            timeTable.setLectureNumber(cell.getLecture());
            timeTable.setDay(cell.getDay());
            timeTable.setGroup(GroupServiceImpl.mapDtoToEntity(cell.getGroupDto()));
            if (Objects.nonNull(cell.getRoomDto())) {
                timeTable.setRoom(RoomServiceImpl.mapDtoToEntity(cell.getRoomDto()));
            } else {
                timeTable.setRoom(null);
            }
            if (Objects.nonNull(cell.getSubjectDto())) {
                timeTable.setSubject(SubjectServiceImpl.mapDtoToEntity(cell.getSubjectDto()));
            } else {
                timeTable.setSubject(null);
            }
            timeTable.setTimeTableDescription(TimeTableDescriptionService.mapDtoToEntity(timeTableDescriptionDto));
            if (i % batchSize() == 0 && i > 0) {
                sessionFactory.getCurrentSession().flush();
                sessionFactory.getCurrentSession().clear();
            }
            timeTableService.create(timeTable);


        }

    }

//    private Course saveCourse(Cell cell) {
//        CourseDto courseDto = cell.getCourseDto();
//        courseDto.setSubjectSet(new HashSet<>());
//        Subject subject = SubjectServiceImpl.mapDtoToEntity(cell.getSubjectDto());
//        return courseService.createAndAddSubjectIfNeeded(courseDto, subject);
//    }
//
//    private Subject saveSubject(Cell cell) {
//        SubjectDto subjectDto = cell.getSubjectDto();
//        return subjectService.createIfNotExists(subjectDto);
//    }
//
//    private Room saveRoom(Cell cell) {
//        RoomDto roomDto = cell.getRoomDto();
//        return roomService.createIfNotExists(roomDto);
//    }
//
//    private Group saveGroup(Cell cell) throws EntityNotFoundException {
//        GroupDto groupDto = cell.getGroupDto();
//        return groupService.create(groupDto);
//    }

    private int batchSize() {
        return Integer.valueOf(Dialect.DEFAULT_BATCH_SIZE);
    }
}
