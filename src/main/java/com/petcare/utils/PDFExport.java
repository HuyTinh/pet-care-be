package com.petcare.utils;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.petcare.dto.response.PetResponse;

import java.util.List;

public class PDFExport
{

    public static void addHeader(Document document) {
        // Tạo một Paragraph với văn bản tiêu đề
        Paragraph header = new Paragraph("Pets Report")
                .setFontSize(24)           // Tăng kích thước font
                .setBold()                 // In đậm chữ
                .setTextAlignment(TextAlignment.CENTER) // Căn giữa
                .setFontColor(ColorConstants.BLUE) // Đổi màu chữ
                .setMarginBottom(15);      // Thêm khoảng cách dưới tiêu đề

        // Tạo đường viền dưới tiêu đề
        LineSeparator lineSeparator = new LineSeparator(new SolidLine());
        lineSeparator.setMarginTop(5);
        lineSeparator.setMarginBottom(10);

        // Thêm tiêu đề và đường viền vào tài liệu
        document.add(header);
        document.add(lineSeparator);
    }

    public static void addBody(Document document, List<PetResponse> pets) {
        // Tạo một đoạn văn giới thiệu
        Paragraph intro = new Paragraph("This report contains a list of pets with their details.")
                .setFontSize(14)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMarginBottom(20); // Thêm khoảng cách dưới đoạn văn

        document.add(intro);

        // Tạo bảng với các cột có tỷ lệ cụ thể
        Table table = new Table(new float[]{3, 3, 1});
        table.setWidth(UnitValue.createPercentValue(100)); // Đặt chiều rộng bảng là 100%

        // Thêm header cho bảng
        table.addHeaderCell(new Cell().add(new Paragraph("Pet ID").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
        table.addHeaderCell(new Cell().add(new Paragraph("Pet Name").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
        table.addHeaderCell(new Cell().add(new Paragraph("Pet Age").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
        table.addHeaderCell(new Cell().add(new Paragraph("Pet Weight").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
        table.addHeaderCell(new Cell().add(new Paragraph("Pet Species").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
        table.addHeaderCell(new Cell().add(new Paragraph("Owner Name").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));

        // Thêm các dòng dữ liệu vào bảng
        for (PetResponse pet : pets) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(pet.getPetId())).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph(pet.getPetName()).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph(pet.getPetAge()).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph(pet.getPetWeight()).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph(pet.getPetSpecies()).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph(pet.getOwnerName()).setFontSize(12)));
        }

        document.add(table);
    }


}
