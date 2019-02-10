package application;

import java.io.Serializable;

public class RedemptionCode implements Serializable{
	
	//Fields
	
	private static final long serialVersionUID = 1L;
	private String code;
	private String studentID;
	
	//Constructors
	
	public RedemptionCode() {
		code = "None";
		studentID = "-1";
	}
	
	public RedemptionCode(String code) {
		this.code = code;
		studentID = "-1";
	}

	//Getters and Setters
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	
}
