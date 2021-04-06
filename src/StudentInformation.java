package commTracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class StudentInformation {
	private final SimpleStringProperty firstName;
	private final SimpleStringProperty lastName;
	private final SimpleStringProperty midName;
	private final SimpleStringProperty studentId;
	private final SimpleIntegerProperty grade;
	private final SimpleDoubleProperty hours;

	
	public StudentInformation(String firstName, String midName, String lastName, 
							String studentId, Integer grade, Double hours) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.midName = new SimpleStringProperty(midName);
		this.studentId = new SimpleStringProperty(studentId);
		this.grade = new SimpleIntegerProperty(grade);
		this.hours = new SimpleDoubleProperty(hours);
	}
	
	public StudentInformation(String firstName, String lastName, String studentId, 
								Integer grade, Double hours) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.studentId = new SimpleStringProperty(studentId);
		this.grade = new SimpleIntegerProperty(grade);
		this.hours = new SimpleDoubleProperty(hours);
		this.midName = new SimpleStringProperty("");;
	}

	public String getFirstName() {
		return firstName.get();
	}
	
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	
	public String getLastName() {
		return lastName.get();
	}
	
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	
	public String getMidName() {
		return midName.get();
	}
	
	public void setMidName(String midName) {
		this.midName.set(midName);
	}
	
	public String getStudentId() {
		return studentId.get();
	}
	
	public void setStudentId(String studentId) {
		this.studentId.set(studentId);
	}

	public Integer getGrade() {
		return grade.get();
	}

	public void setGrade(Integer grade) {
		this.grade.set(grade);
	}
	
	public Double getHours() {
		return hours.get();
	}

	public void setHours(Double hours) {
		this.hours.set(hours);
	}
  
}