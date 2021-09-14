package com.honsoft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.honsoft.entity.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long>{

	List<Assignment> findByStudentId(Long studentId);
}
