package com.honsoft.controller;

import java.util.List;
import java.util.Optional;

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

import com.honsoft.entity.Student;
import com.honsoft.repository.StudentRepository;

import javassist.NotFoundException;

@RestController
@RequestMapping("/api")
public class StudentController {
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping("/students")
	public List<Student> retrieveAllStudents() {
		return studentRepository.findAll();
	}
	
	@GetMapping("/students/{id}")
	public Student getStudentById(@PathVariable Long id) throws NotFoundException {
		Optional optStudent = studentRepository.findById(id);
		if(optStudent.isPresent()) {
			return (Student) optStudent.get();
		}else {
			throw new NotFoundException("Student not found with id "+id);
		}
	}
	
	@PostMapping("/students")
	public Student createStudent(@RequestBody Student student) {
		return studentRepository.save(student);
	}
	
	@PutMapping("/students/{id}")
	public Student updateStudent(@PathVariable Long id, @RequestBody Student studentUpdated) throws NotFoundException {
		return studentRepository.findById(id).map(student -> {
			student.setName(studentUpdated.getName());
			student.setAge(studentUpdated.getAge());
			return studentRepository.save(student);
		}).orElseThrow(() -> new NotFoundException("Student not found with id" + id));
	}
	
	@DeleteMapping("/students/{id}")
	public String deleteStudent(@PathVariable Long id) throws NotFoundException {
		return studentRepository.findById(id).map(student -> {
			studentRepository.delete(student);
			return "delete successfully";
			
		}).orElseThrow(() -> new NotFoundException("Student not found with id "+id));
	}
}
	