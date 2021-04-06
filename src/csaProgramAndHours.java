package commTracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class csaProgramAndHours {
	private final SimpleStringProperty category;
	private final SimpleDoubleProperty hours;

  
	public csaProgramAndHours(String category, Double hours) {
		this.category = new SimpleStringProperty(category);
		this.hours = new SimpleDoubleProperty(hours);
	}

	public String getCategory() {
		return category.get();
	}

	public void setCategory(String category) {
		this.category.set(category);
	}

	public Double getHours() {
		return hours.get();
	}

	public void setHours(Double hours) {
		this.hours.set(hours);
	}
  
}