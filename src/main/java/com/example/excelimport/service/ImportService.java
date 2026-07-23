package com.example.excelimport.service;
import com.example.excelimport.dto.ImportResult;
import com.example.excelimport.entity.Student;
import com.example.excelimport.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportService {
    private final StudentRepository studentRepository;
    @Value("${app.chunk.size:100}")
    private int chunksize;
    private String getCellValue(Row row, int cellIndex){
        Cell cell=row.getCell(cellIndex);
        if(cell==null){
            return "";
        }
        return switch (cell.getCellType()){
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
    private Integer getIntCellValue(Row row, int cellIndex){
        Cell cell= row.getCell(cellIndex);
        if (cell==null){
            return  null;
        }
        return switch (cell.getCellType()){
            case NUMERIC -> (int) cell.getNumericCellValue();
            case STRING -> Integer.parseInt(cell.getStringCellValue());
            default -> null;
        };
    }
    public ImportResult importStudents(MultipartFile file )throws IOException{
        int totalRows=0;
        int successCount=0;
        int errorCount=0;
        List<Student> chunk=new ArrayList<>();
        Workbook workbook=new XSSFWorkbook(file.getInputStream());
        Sheet sheet=workbook.getSheetAt(0);
        log.info("total row: {}", sheet.getLastRowNum());
        for (int rowIndex=1; rowIndex<=sheet.getLastRowNum(); rowIndex++){
            Row row=sheet.getRow(rowIndex);
            if(row==null){
                continue;
            }
            totalRows++;
            try {
                Student student=Student.builder()
                        .name(getCellValue(row,0))
                        .email(getCellValue(row,1))
                        .age(getIntCellValue(row,2))
                        .build();
                chunk.add(student);
                successCount++;
                if(chunk.size()>=chunksize){
                    studentRepository.saveAll(chunk);
                    log.info("{} in db(total:{})", chunk.size(), successCount);
                    chunk.clear();
                }
            }catch (Exception e){
                errorCount++;
                log.error("row:{},error:{}",rowIndex,e.getMessage() );

            }
        }
        if(!chunk.isEmpty()){
            studentRepository.saveAll(chunk);
            log.info("last chunk:{}", chunk.size());
        }
        workbook.close();
        log.info("import finished, total:{},success:{}, Error:{}", totalRows, successCount, errorCount);
        return ImportResult.builder().totalRows(totalRows).successfulCount(successCount).errorCount(errorCount).message("impoert finished").build();
    }

}
