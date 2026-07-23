package com.example.excelimport.controller;
import com.example.excelimport.dto.ImportResult;
import com.example.excelimport.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    private  final ImportService ImportService;
    @PostMapping(value = "/students", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportResult> importStudents(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        String fileName= file.getOriginalFilename();
        if(fileName==null || !fileName.endsWith(".xlsx")){
            return ResponseEntity.badRequest().build();
        }
        try {
            ImportResult result= ImportService.importStudents(file);
            return ResponseEntity.ok(result);
        }catch(Exception e){
            e.printStackTrace();
            return  ResponseEntity.internalServerError().build();
        }
    }
}
