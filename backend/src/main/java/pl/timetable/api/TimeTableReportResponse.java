package pl.timetable.api;

import pl.timetable.dto.ReportPopulationDto;

import java.util.List;

public class TimeTableReportResponse extends BaseResponse {

    List<ReportPopulationDto> reportPopulation;

    public List<ReportPopulationDto> getReportPopulation() {
        return reportPopulation;
    }

    public void setReportPopulation(List<ReportPopulationDto> reportPopulation) {
        this.reportPopulation = reportPopulation;
    }
}
