package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.ReportPopulationRequest;
import pl.timetable.dto.ReportPopulationDto;
import pl.timetable.entity.ReportPopulation;
import pl.timetable.entity.TimeTableDescription;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.ReportPopulationRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportPopulationService extends AbstractService<ReportPopulationDto, ReportPopulationRequest> {

    public static final Logger LOGGER = Logger.getLogger(ReportPopulationService.class);

    private ReportPopulationRepository reportPopulationRepository;

    private TimeTableDescriptionService timeTableDescriptionService;

    @Autowired
    public void setReportPopulationRepository(ReportPopulationRepository reportPopulationRepository) {
        this.reportPopulationRepository = reportPopulationRepository;
    }

    @Autowired
    public void setTimeTableDescriptionService(TimeTableDescriptionService timeTableDescriptionService) {
        this.timeTableDescriptionService = timeTableDescriptionService;
    }

    @Override
    public List<ReportPopulationDto> findAll() {
        List<ReportPopulation> reportPopulations = reportPopulationRepository.findAll().orElse(Collections.emptyList());
        return reportPopulations.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void create(ReportPopulationRequest request) throws EntityNotFoundException {

    }

    public void create(ReportPopulationDto reportPopulationDto) throws EntityNotFoundException {
        ReportPopulation reportPopulation = mapDtoToEntity(reportPopulationDto);
        reportPopulationRepository.create(reportPopulation);
    }

    @Override
    public ReportPopulationDto update(ReportPopulationRequest request, int id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {

    }

    @Override
    public ReportPopulationDto get(int id) throws EntityNotFoundException {
        return null;
    }

    public List<ReportPopulationDto> getByTimeTableDescription(Integer timeTableDescriptionId){
        return reportPopulationRepository.getByTimeTableDescription(timeTableDescriptionId).stream()
                .map(this::mapEntityToDto).collect(Collectors.toList());
    }

    private ReportPopulationDto mapEntityToDto(ReportPopulation reportPopulation){
        ReportPopulationDto reportPopulationDto = new ReportPopulationDto();
        reportPopulationDto.setBestFitnessScore(reportPopulation.getBestFitnessScore());
        reportPopulationDto.setBestHardFitnessScore(reportPopulation.getBestHardFitnessScore());
        reportPopulationDto.setBestSoftFitnessScore(reportPopulation.getBestSoftFitnessScore());
        reportPopulationDto.setPopulationGeneration(reportPopulation.getPopulationGeneration());
        reportPopulationDto.setTimeTableDescriptionId(reportPopulation.getTimeTableDescription().getId());
        reportPopulationDto.setPopulationSize(reportPopulation.getPopulationSize());
        return reportPopulationDto;
    }

    private ReportPopulation mapDtoToEntity(ReportPopulationDto reportPopulationDto) throws EntityNotFoundException {
        ReportPopulation reportPopulation = new ReportPopulation();
        reportPopulation.setBestFitnessScore(reportPopulationDto.getBestFitnessScore());
        reportPopulation.setBestHardFitnessScore(reportPopulationDto.getBestHardFitnessScore());
        reportPopulation.setBestSoftFitnessScore(reportPopulationDto.getBestSoftFitnessScore());
        reportPopulation.setPopulationGeneration(reportPopulationDto.getPopulationGeneration());
        reportPopulation.setPopulationSize(reportPopulationDto.getPopulationSize());
        reportPopulation.setTimeTableDescription(TimeTableDescriptionService.mapDtoToEntity(timeTableDescriptionService.get(reportPopulationDto.getTimeTableDescriptionId())));
        return reportPopulation;
    }
}
