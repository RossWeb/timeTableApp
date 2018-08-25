package pl.timetable.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.timetable.api.TimeTableInitResponse;
import pl.timetable.api.TimeTableRequest;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.dto.Population;
import pl.timetable.entity.TimeTableDescription;
import pl.timetable.enums.TimeTableDescriptionStatus;
import pl.timetable.facade.TimeTableFacade;
import pl.timetable.service.GeneticAlgorithmService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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
    public ResponseEntity<TimeTableInitResponse> genericTimeTable(@RequestBody TimeTableRequest timeTableRequest){
        LOGGER.info("Try to generic timetable");
        GeneticInitialData geneticInitialData = timeTableFacade.getGeneticInitialData(timeTableRequest);
        Integer timeTableDescriptionId = geneticAlgorithmService.init(geneticInitialData);
        if(Objects.isNull(timeTableDescriptionId)){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new TimeTableInitResponse("INIT_FAILED"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new TimeTableInitResponse(timeTableDescriptionId));
    }

    @GetMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String,String>> getTimeTableStatus(@PathVariable("id") Integer timeTableId){
        LOGGER.info("Try to get status for timeTableId : " + timeTableId);
        TimeTableDescriptionStatus status = timeTableFacade.getTimeTableDescriptionStatus(timeTableId);
        if(Objects.nonNull(status)){
            Map<String, String> map = new LinkedHashMap<>();
            map.put("status", status.name());
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }

    }
}
