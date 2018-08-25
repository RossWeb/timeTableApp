package pl.timetable.service;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.timetable.DebugService;
import pl.timetable.dto.*;
import pl.timetable.entity.TimeTableDescription;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.facade.TimeTableFacade;
import pl.timetable.repository.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static pl.timetable.service.GeneticUtilityTestClass.getGeneticInitialData;
@RunWith(SpringRunner.class)
@DataJpaTest
public class GeneticAlgorithmServiceTestIT {

    @TestConfiguration
    static class GeneticAlghoritmTestContextConfiguration {

        @Bean
        public RoomRepository roomRepository() {
            return new RoomRepositoryImpl();
        }
        @Bean
        public CourseRepository courseRepository() {
            return new CourseRepositoryImpl();
        }
        @Bean
        public GroupRepository groupRepository() {
            return new GroupRepositoryImpl();
        }

        @Bean
        public CourseServiceImpl courseService(){
            return new CourseServiceImpl();
        }
        @Bean
        public RoomServiceImpl roomService(){
            return new RoomServiceImpl(new RoomRepositoryImpl());
        }
        @Bean
        public SubjectRepository subjectRepository() {return new SubjectRepositoryImpl();}

        @Bean
        public SubjectServiceImpl subjectService(){
            return new SubjectServiceImpl(new SubjectRepositoryImpl());
        }
        @Bean
        public TimeTableDescriptionRepository timeTableDescriptionRepository(){
            return new TimeTableDescriptionRepositoryImpl();
        }
        @Bean
        public TimeTableDescriptionService timeTableDescriptionService(){
            return new TimeTableDescriptionService(timeTableDescriptionRepository());
        }
        @Bean
        public TimeTableRepository timeTableRepository(){
            return new TimeTableRepositoryImpl();
        }
        @Bean
        public GroupServiceImpl groupService(){
            return new GroupServiceImpl(groupRepository(), courseRepository());
        }
        @Bean
        public LectureDescriptionRepository lectureDescriptionRepository(){
            return new LectureDescriptionRepositoryImpl();
        }
        @Bean
        public LectureDescriptionService lectureDescriptionService(){
            return new LectureDescriptionService(new LectureDescriptionRepositoryImpl(), timeTableDescriptionService());
        }
        @Bean
        public TimeTableService timeTableService(){
            return new TimeTableService(groupService(), roomService(), subjectService(), timeTableDescriptionService(), timeTableRepository());
        }
        @Bean
        public GeneticAlgorithmService geneticAlgorithmService(){
            return new GeneticAlgorithmService(null, null, null);
        }
        @Bean
        public DebugService debugService(){return new DebugService();}

        @Bean
        public ReportPopulationRepository reportPopulationRepository(){
            return new ReportPopulationRepositoryImpl();
        }

        @Bean
        public ReportPopulationService reportPopulationService(){
            return new ReportPopulationService();
        }

        @Bean
        public TimeTableFacade timeTableFacade() {
            return new TimeTableFacade( timeTableService(), lectureDescriptionService(),timeTableDescriptionService(),
                    reportPopulationService(), groupService(), roomService(), subjectService(),courseService());}
    }

    @Autowired
    private GeneticAlgorithmService geneticAlgorithmService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeTableFacade timeTableFacade;

    @Autowired
    private TimeTableDescriptionRepository timeTableDescriptionRepository;
    @Autowired
    private TimeTableDescriptionService timeTableDescriptionService;
    @Autowired
    private LectureDescriptionService lectureDescriptionService;
    @Autowired
    private LectureDescriptionRepository lectureDescriptionRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectServiceImpl subjectService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomServiceImpl roomService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DebugService debugService;
    @Autowired
    private TimeTableService timeTableService;

    @Before
    public void init(){
        HardGenotypeCriteria hardGenotypeCriteria = new HardGenotypeCriteria();
        SoftGenotypeCriteria softGenotypeCriteria = new SoftGenotypeCriteria();
        GenotypeService genotypeService = new GenotypeService(hardGenotypeCriteria);
        genotypeService.setHardGenotypeCriteria(hardGenotypeCriteria);
        FitnessService fitnessService = new FitnessService(hardGenotypeCriteria, softGenotypeCriteria);
        geneticAlgorithmService.setGenotypeService(genotypeService);
        geneticAlgorithmService.setFitnessService(fitnessService);
        geneticAlgorithmService.setTimeTableFacade(timeTableFacade);
        timeTableDescriptionService.setTimeTableDescriptionRepository(timeTableDescriptionRepository);
        lectureDescriptionService.setLectureDescriptionRepository(lectureDescriptionRepository);
        subjectService.setSubjectRepository(subjectRepository);
        roomService.setRoomRepository(roomRepository);
        debugService.setRoomRepository(roomRepository);
        debugService.setSubjectRepository(subjectRepository);
        debugService.setCourseRepository(courseRepository);
        debugService.setGroupRepository(groupRepository);
    }

    @Test
    //uwzlednic async !
    public void initTest() throws EntityNotFoundException {
        //given
        GeneticInitialData geneticInitialData = getGeneticInitialData();
        debugService.init(geneticInitialData);
        geneticInitialData.setPopulationSize(200);
        //when
        Integer timeTableId = geneticAlgorithmService.init(geneticInitialData);
        //then
        Assert.assertNotNull(timeTableId);
        TimeTableDescriptionDto timeTableDescriptionDto = timeTableDescriptionService.findAll().get(0);
        Assert.assertThat(timeTableDescriptionDto, notNullValue());
        LectureDescriptionDto lectureDescriptionDto = lectureDescriptionService.getLectureDescriptionByTimeTableDescription(timeTableDescriptionDto);
        Assert.assertThat(lectureDescriptionDto, notNullValue());
        List<TimeTableDto> timeTableDtoList= timeTableService.findAll();
        Assert.assertThat(timeTableDtoList.isEmpty(), is(false));
        List<ReportPopulationDto>  reportPopulationDtos = timeTableFacade.getReportPopulation(timeTableDescriptionDto.getId());
        Assert.assertThat(reportPopulationDtos.isEmpty(), is(false));


    }

}
