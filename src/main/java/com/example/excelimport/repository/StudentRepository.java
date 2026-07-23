package com.example.excelimport.repository;
import com.example.excelimport.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface StudentRepository extends JpaRepository<Student,Long>{
}
