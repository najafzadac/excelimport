package com.example.excelimport.controller;
import com.example.excelimport.dto.ImportResult;
import com.example.excelimport.service.ImportService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    @PostMapping(value = "/students/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportResult> importFile(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return ResponseEntity.badRequest().body(ImportResult.builder().success(false).message("select a file to upload").build());
        }
        String fileName=file.getOriginalFilename();
        if (fileName==null|| (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))){
            return ResponseEntity.badRequest().body(ImportResult.builder().success(false).message("upload .xlsx or .xls").build());
        }
        ImportResult result=ImportService.importExcelFile(file);
        return result.getSuccess() ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }


}
