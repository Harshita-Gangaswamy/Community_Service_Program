package commTracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class StudentHoursLog {
	private final SimpleStringProperty date;
	private final SimpleDoubleProperty hours;

  
	public StudentHoursLog(String date, Double hours) {
		this.date = new SimpleStringProperty(date);
		this.hours = new SimpleDoubleProperty(hours);
	}

	public String getDate() {
		return date.get();
	}

	public void setDate(String date) {
		this.date.set(date);
	}

	public Double getHours() {
		return hours.get();
	}

	public void setHours(Double hours) {
		this.hours.set(hours);
	}
  
}