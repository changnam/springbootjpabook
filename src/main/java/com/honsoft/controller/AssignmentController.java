package com.honsoft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honsoft.entity.Assignment;
import com.honsoft.repository.AssignmentRepository;
import com.honsoft.repository.StudentRepository;

import javassist.NotFoundException;

@RestController
@RequestMapping("/api")
public class AssignmentController {
	@Autowired
	private AssignmentRepository assignmentRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping("/students/{studentId}/assignments")
	public List getAssignmentsByStudentId(@PathVariable Long studentId) throws NotFoundException {
		if (!studentRepository.existsById(studentId)) {
			throw new NotFoundException("student not found");
		}
		
		return assignmentRepository.findByStudentId(studentId);
	}
	
	@PostMapping("/students/{studentId}/assignments")
	public Assignment addAssignment(@PathVariable Long studentId, @RequestBody Assignment assignment) throws NotFoundException {
		return studentRepository.findById(studentId).map(student -> {
			assignment.setStudent(student);
			return assignmentRepository.save(assignment);
		}).orElseThrow(() -> new NotFoundException("student not found with id "+studentId));
	}
	
	@PutMapping("/students/{studentId}/assignments/{assignmentId}")
	public Assignment updateAssignment(@PathVariable Long studentId, @PathVariable Long assignmentId, @RequestBody Assignment assignmentUpdated) throws NotFoundException {
		if(!studentRepository.existsById(studentId)) {
			throw new NotFoundException("student not found with id "+studentId);
		}
		
		return assignmentRepository.findById(assignmentId).map(assignment -> {
			assignment.setName(assignmentUpdated.getName());
			assignment.setGrade(assignmentUpdated.getGrade());
			return assignmentRepository.save(assignment);
		}).orElseThrow(() -> new NotFoundException("assignment not found with id "+assignmentId));
		
	}
	
	@DeleteMapping("/students/{studentId}/assignments/{assignmentId}")
	public String deleteAssignment(@PathVariable Long studentId, @PathVariable Long assignmentId) throws NotFoundException {
		if(!studentRepository.existsById(studentId)) {
			throw new NotFoundException("student not found with id "+studentId);
		}
		
		return assignmentRepository.findById(assignmentId).map(assignment -> {
			assignmentRepository.delete(assignment);
			return "deleted successfully";
		}).orElseThrow(() -> new NotFoundException("assignment not found with id "+ assignmentId));
	}
}
