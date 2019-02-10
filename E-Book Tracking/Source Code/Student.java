package application;

import java.io.Serializable;

public class Student implements Serializable{
	
	//Fields
	
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private int grade;
	private String studentID;
	
	public Student() {
		firstName = "";
		lastName = "";
		grade = 0;
		studentID = "";
	}
	
	public Student(String firstName, String lastName, int grade, String studentID) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.grade = grade;
		this.studentID = studentID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	
}
