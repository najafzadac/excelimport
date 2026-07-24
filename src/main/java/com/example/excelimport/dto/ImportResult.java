package com.example.excelimport.dto;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ImportResult{
    private Boolean success;
    private String message;
    private List<String> errors;


}
