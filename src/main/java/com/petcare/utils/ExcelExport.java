package com.petcare.utils;

import com.petcare.dto.response.PetResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.util.List;

public class ExcelExport {

    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;
    private static String[] HEADER = {
            "Pet ID",
            "Pet Name",
            "Pet Age",
            "Pet Weight",
            "Pet Species",
            "Owner Name",
    };
    private static String SHEET_NAME = "list pet";

    public static ByteArrayInputStream exportExcel(List<PetResponse> petResponses) throws IOException {
        // create workbook
        workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // create sheet
            sheet = workbook.createSheet(SHEET_NAME);

            // setting style

            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER); // căn giữa ngang
            style.setVerticalAlignment(VerticalAlignment.CENTER); // căn giữa dọc
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setWrapText(true); // Ngắt dòng trong ô

            // Set header
            setHeader(style);

            // Set values
            setValues(petResponses, style);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            workbook.close();
            out.close();
        }
    }

    private static void setHeader(CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < HEADER.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADER[i]);
            cell.setCellStyle(headerStyle); // Apply header style
        }
    }

    private static void setValues(List<PetResponse> petResponses, CellStyle dataStyle) {
        int rowIndex = 1;

        for (PetResponse petResponse : petResponses) {
            Row dataRow = sheet.createRow(rowIndex);
            rowIndex++;
            dataRow.createCell(0).setCellValue(petResponse.getPetId());
            dataRow.createCell(1).setCellValue(petResponse.getPetName());
            dataRow.createCell(2).setCellValue(petResponse.getPetAge());
            dataRow.createCell(3).setCellValue(petResponse.getPetWeight());
            dataRow.createCell(4).setCellValue(petResponse.getPetSpecies());
            dataRow.createCell(5).setCellValue(petResponse.getOwnerName());

            for (int i = 0; i < HEADER.length; i++) {
                dataRow.getCell(i).setCellStyle(dataStyle);
                sheet.autoSizeColumn(i);
            }
        }
    }

}