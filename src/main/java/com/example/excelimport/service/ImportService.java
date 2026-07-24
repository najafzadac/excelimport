package com.example.excelimport.service;
import com.example.excelimport.entity.Student;
import com.example.excelimport.dto.ImportResult;
import com.example.excelimport.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.io.InputStream;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportService {
    private final StudentRepository studentRepository;
    private String getCellString(Cell cell){
        if (cell==null)return null;
        if(cell.getCellType()==CellType.STRING){
            return cell.getStringCellValue().trim();
        }
        if (cell.getCellType()==CellType.NUMERIC){
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return null;
    }
    private Integer getCellInt(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            }
            if (cell.getCellType() == CellType.STRING) {
                return Integer.parseInt(cell.getStringCellValue().trim());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    public ImportResult importExcelFile(MultipartFile file){
        List<Student> students=new ArrayList<>();
        List<String>errors=new ArrayList<>();
        int rowNumber=0;
        try(InputStream is=file.getInputStream(); Workbook workbook=new XSSFWorkbook(is)){
            Sheet sheet=workbook.getSheetAt(0);
            log.info("processing:{}", file.getOriginalFilename());
            for (Row row:sheet){
                rowNumber++;
                if(rowNumber==1)continue;
                try {
                    String name=getCellString(row.getCell(0));
                    String email=getCellString(row.getCell(1));
                    Integer age=getCellInt(row.getCell(2));
                    if (name==null || name.isEmpty()){
                        errors.add("Row"+rowNumber+"required name");
                        continue;
                    }
                    if (email==null || email.isEmpty()){
                        errors.add("row"+rowNumber+"email required");
                        continue;
                    }
                    Student student=Student.builder().name(name).email(email).age(age).build();
                    students.add(student);
                }catch (Exception e){
                    errors.add("row"+rowNumber+ e.getMessage());
                }

            }if(!students.isEmpty()){
                studentRepository.saveAll(students);
                log.info("saved:{}", students.size());
            }

        }catch (Exception e){
            log.error("failed to read", e);
            return ImportResult.builder().message("failed to read"+e.getMessage()).errors(errors).success(false).build();
        }
        return ImportResult.builder().success(errors.isEmpty()).message(errors.isEmpty()? "imported succesful": "impoerted with errors").errors(errors.isEmpty()?null:errors).build();


    }

}
