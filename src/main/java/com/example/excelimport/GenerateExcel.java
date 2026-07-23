package com.example.excelimport;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

public class GenerateExcel {

    public static void main(String[] args) throws Exception {

        System.out.println("yaradilir");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("students");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("fullName");
        header.createCell(1).setCellValue("email");
        header.createCell(2).setCellValue("age");
        for (int i = 1; i <= 100000; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue("student " + i);
            row.createCell(1).setCellValue("student" + i + "@test.com");
            row.createCell(2).setCellValue(20 + (i % 10));

            if (i % 10000 == 0) {
                System.out.println(i + " row");
            }
        }

        FileOutputStream out = new FileOutputStream("students.xlsx");
        workbook.write(out);
        workbook.close();
        out.close();

        System.out.println("Hazirdir: students.xlsx");
    }
}