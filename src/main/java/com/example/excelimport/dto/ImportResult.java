package com.example.excelimport.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ImportResult{
    private int totalRows;
    private int successfulCount;
    private int errorCount;
    private String message;

}
