package com.honsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.honsoft.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

}
