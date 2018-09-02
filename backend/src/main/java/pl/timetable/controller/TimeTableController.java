package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.timetable.api.*;
import pl.timetable.dto.*;
import pl.timetable.enums.TimeTableDescriptionStatus;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.facade.TimeTableFacade;
import pl.timetable.service.GeneticAlgorithmService;
import pl.timetable.service.RoomServiceImpl;
import pl.timetable.service.SubjectServiceImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timetable")
public class TimeTableController {

    private static final Logger LOGGER = Logger.getLogger(TimeTableController.class);

    private GeneticAlgorithmService geneticAlgorithmService;
    private TimeTableFacade timeTableFacade;

    public TimeTableController(GeneticAlgorithmService geneticAlgorithmService, TimeTableFacade timeTableFacade) {
        this.geneticAlgorithmService = geneticAlgorithmService;
        this.timeTableFacade = timeTableFacade;
    }

    @PostMapping("/init")
    @ResponseBody
    public ResponseEntity<TimeTableInitResponse> genericTimeTable(@RequestBody TimeTableRequest timeTableRequest) {
        LOGGER.info("Try to generic timetable");
        GeneticInitialData geneticInitialData = timeTableFacade.getGeneticInitialData(timeTableRequest);
        Integer timeTableDescriptionId = geneticAlgorithmService.init(geneticInitialData);
        if (Objects.isNull(timeTableDescriptionId)) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new TimeTableInitResponse("INIT_FAILED"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new TimeTableInitResponse(timeTableDescriptionId));
    }

    @GetMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getTimeTableStatus(@PathVariable("id") Integer timeTableId) {
        LOGGER.info("Try to get status for timeTableId : " + timeTableId);
        TimeTableDescriptionStatus status = timeTableFacade.getTimeTableDescriptionStatus(timeTableId);
        if (Objects.nonNull(status)) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("status", status.name());
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }

    }

    @PostMapping("/{id}/result")
    @ResponseBody
    public ResponseEntity<TimeTableResultResponse> getTimeTableResult(@PathVariable("id") Integer timeTableId, @RequestBody TimeTableResultRequest timeTableResultRequest) {
        LOGGER.info("Try to get result for timeTableId : " + timeTableId);
        TimeTablePagingDto timeTablePagingDto = new TimeTablePagingDto();
        timeTablePagingDto.setId(timeTableId);
        timeTablePagingDto.setPageNumber(timeTableResultRequest.getPageNumber());
        timeTablePagingDto.setSize(timeTableResultRequest.getSize());
        timeTablePagingDto.setGroupId(timeTableResultRequest.getGroupId());
        timeTablePagingDto.setDays(timeTableResultRequest.getDays());
        try {
            TimeTableResultDto timeTableResultDto = timeTableFacade.getTimeTableResult(timeTablePagingDto);
            TimeTableResultResponse resultResponse = new TimeTableResultResponse();
            resultResponse.setData(timeTableResultDto.getTimeTableDtos().stream().map(timeTableDto -> {
                TimeTableResponse response = new TimeTableResponse();
                response.setLectureNumber(timeTableDto.getLectureNumber());
                if(Objects.nonNull(timeTableDto.getRoom())) {
                    response.setRoom(RoomServiceImpl.mapEntityToDto(timeTableDto.getRoom()));
                }
                if(Objects.nonNull(timeTableDto.getSubject())) {
                    response.setSubject(SubjectServiceImpl.mapEntityToDto(timeTableDto.getSubject()));
                }
                response.setDay(timeTableDto.getDay());
                return response;
            }).collect(Collectors.toList()));
            resultResponse.setTotalElements(timeTableResultDto.getTotalElements());
            resultResponse.setTotalPages(timeTableResultDto.getTotalPages());
            return ResponseEntity.status(HttpStatus.OK).body(resultResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @GetMapping("{id}/report")
    @ResponseBody
    public ResponseEntity<TimeTableReportResponse> getReport(@PathVariable("id") Integer timeTableId) {
        LOGGER.info("Try to get report by timeTableId" + timeTableId);
        List<ReportPopulationDto> reportPopulationDtoList = timeTableFacade.getReportPopulation(timeTableId);
        if (reportPopulationDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            TimeTableReportResponse reportResponse = new TimeTableReportResponse();
            reportResponse.setReportPopulation(reportPopulationDtoList);
            return ResponseEntity.status(HttpStatus.OK).body(reportResponse);
        }

    }
}
