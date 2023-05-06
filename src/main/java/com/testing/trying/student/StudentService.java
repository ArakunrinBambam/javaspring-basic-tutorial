package com.testing.trying.student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService 
{
	private final StudentRepository studentRepository;

	public StudentService(StudentRepository studentRepository)
	{
		this.studentRepository = studentRepository;
	}


    public List<Student> getStudents()
	{
		return studentRepository.findAll();
	}

	public void addNewStudent(Student student)
	{
		Optional<Student> optionalStudent = studentRepository.findStudentByEmail(student.getEmail());

		if(optionalStudent.isPresent()){

			throw new IllegalStateException("Email is taken");
		}

		studentRepository.save(student);
	}

	public void deleteStudent(Long id)
	{
		boolean exists = studentRepository.existsById(id);

		if (!exists) {

			throw new IllegalStateException("Student with id " + id + " does not exist");
		}

		studentRepository.deleteById(id);

	}

	@Transactional
	public void updateStudent(Long id, String name, String email)
	{
		Student student = studentRepository.findById(id)
				.orElseThrow(()-> new IllegalStateException(
					"Student with id "+ id + "doest not exists"));
	
		
		if(name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) 
		{
			student.setName(name);
		}

		if(email !=null && email.length()> 0 && !Objects.equals(student.getEmail(), email))
		{
			Optional<Student> optionalStudent = studentRepository.findStudentByEmail(email);
			if(optionalStudent.isPresent()){
				throw new IllegalStateException("Email is already taken");
			}
			student.setEmail(email);
		}



	}
}
