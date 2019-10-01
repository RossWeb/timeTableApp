package pl.timetable.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import pl.timetable.dto.LectureDescriptionDto;
import pl.timetable.dto.TimeTableDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorkBookService {

    private static Map<IndexedColors, CellStyle> colors = new HashMap<>();

    public static Workbook createWorkBookByGenotype(List<TimeTableDto> tableDtos, LectureDescriptionDto lectureDescriptionDto) {
        Workbook wb = new HSSFWorkbook();
        colors = new HashMap<>();
        Sheet sheet = wb.createSheet("Plan zajec");
        Row titleRow = sheet.createRow(sheet.getLastRowNum() + 1);
        titleRow.createCell(0);

        boolean prepareTitle = true;
        for (TimeTableDto cell : tableDtos) {
            Row groupRow = sheet.getRow(cell.getGroup().getId() + 1);
            if (Objects.isNull(groupRow)) {
                groupRow = sheet.createRow(cell.getGroup().getId() + 1);

            }
            Integer lecture = cell.getLectureNumber();
            IndexedColors color = groupRow.getRowNum() % 2 == 0 ? IndexedColors.GREY_40_PERCENT : IndexedColors.GREY_25_PERCENT;
            groupRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            if (lecture == 0) {
                createWorkBookCell(wb, groupRow, cell.getGroup().getName()
                        , HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, color, true);
            } else if (lecture != titleRow.getLastCellNum() - 1) {
                prepareTitle = true;
            }
            boolean borderRight = lecture % lectureDescriptionDto.getNumberPerDay() == 0;
            if (prepareTitle) {
                lecture++;
                titleRow.setHeightInPoints(2 * sheet.getDefaultRowHeightInPoints());
                createWorkBookCell(wb, titleRow, "Dzień " + cell.getDay() + "\n Zajęcia numer " + lecture,
                        HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, IndexedColors.WHITE, borderRight);
                prepareTitle = false;
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
        try {
            Cell workbookCell = row.createCell(row.getPhysicalNumberOfCells());
            workbookCell.setCellValue(data);

            CellStyle cellStyle = colors.get(colorIndex);


            if(Objects.isNull(cellStyle)){
                cellStyle = getCellStyleByColor(wb, halign, valign, colorIndex);
                colors.put(colorIndex, cellStyle);
            }

            if (borderRight) {
                cellStyle.setBorderRight(BorderStyle.THIN);
            }
//            cellStyle.setAlignment(halign);
//            cellStyle.setVerticalAlignment(valign);
//            cellStyle.setWrapText(true);
//            cellStyle.setFillForegroundColor(colorIndex.getIndex());
//            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setFillBackgroundColor(colorIndex.getIndex());
//        cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
            workbookCell.setCellStyle(cellStyle);
        } catch (Exception e) {
            System.out.println();
        }
    }

    private static CellStyle getCellStyleByColor(Workbook wb,
                                                 HorizontalAlignment halign, VerticalAlignment valign,
                                                 IndexedColors colorIndex
                                                 ){
        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setWrapText(true);
        cellStyle.setFillForegroundColor(colorIndex.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setFillBackgroundColor(colorIndex.getIndex());
//        cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
        return cellStyle;
    }
}
