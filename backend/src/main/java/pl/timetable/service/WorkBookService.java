package pl.timetable.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import pl.timetable.dto.LectureDescriptionDto;
import pl.timetable.dto.TimeTableDto;
import pl.timetable.entity.Group;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class WorkBookService {

    public static Workbook createWorkBookByGenotype(List<TimeTableDto> tableDtos, LectureDescriptionDto lectureDescriptionDto) {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Plan zajec");
        Row titleRow = sheet.createRow(sheet.getLastRowNum() + 1);
        titleRow.createCell(0);
        Group actualGroup = null;
        boolean prepareTitle = true;
        for (TimeTableDto cell : tableDtos) {
            Row groupRow = sheet.createRow(sheet.getLastRowNum() + 1);
            IndexedColors color = sheet.getLastRowNum() % 2 == 0 ? IndexedColors.GREY_40_PERCENT : IndexedColors.GREY_25_PERCENT;
            groupRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            if (actualGroup != cell.getGroup()) {
                if(Objects.nonNull(actualGroup)) {
                    prepareTitle = false;
                }
                actualGroup = cell.getGroup();
                createWorkBookCell(wb, groupRow, cell.getGroup().getName() + "\n" + cell.getGroup().getName()
                        , HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, color, true);
            }
            Integer lecture = cell.getLectureNumber();
            lecture++;
            boolean borderRight = lecture % lectureDescriptionDto.getNumberPerDay() == 0;
            if(prepareTitle) {
                titleRow.setHeightInPoints(2 * sheet.getDefaultRowHeightInPoints());
                createWorkBookCell(wb, titleRow, "Dzień " + cell.getDay() + "\n Zajęcia numer " + lecture,
                        HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, IndexedColors.WHITE, borderRight);
            }
            String data = "";
            if (Objects.nonNull(cell.getRoom())) {
                data = new StringBuilder()
                        .append("Przedmiot ").append(cell.getSubject().getName())
                        .append("\n Wykładowca ").append(cell.getTeacher().getName()).append(" ").append(cell.getTeacher().getSurname())
                        .append("\n Sala ").append(cell.getRoom().getName()).append(" ").append(cell.getRoom().getNumber()).toString();
            }

            createWorkBookCell(wb, groupRow, data, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, color, borderRight);

        }
        for (int i = 0; i < sheet.getRow(2).getPhysicalNumberOfCells(); i++) {
            sheet.autoSizeColumn(i);
        }

//        try (OutputStream fileOut = new FileOutputStream("workbook.xls")) {
//            wb.write(fileOut);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return wb;
    }

    private static void createWorkBookCell(Workbook wb, Row row, String data,
                                           HorizontalAlignment halign, VerticalAlignment valign,
                                           IndexedColors colorIndex,
                                           boolean borderRight) {
        Cell workbookCell = row.createCell(row.getPhysicalNumberOfCells());
        workbookCell.setCellValue(data);
        CellStyle cellStyle = wb.createCellStyle();
        if (borderRight) {
            cellStyle.setBorderRight(BorderStyle.THIN);
        }
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setWrapText(true);
        cellStyle.setFillForegroundColor(colorIndex.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setFillBackgroundColor(colorIndex.getIndex());
//        cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
        workbookCell.setCellStyle(cellStyle);
    }
}
