package commTracker;

import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.time.temporal.WeekFields;

import com.mongodb.client.FindIterable; 
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.Updates;
import java.util.Iterator; 
import org.bson.Document;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class monthlyStudentReportController implements Initializable {
	
	@FXML
	private ComboBox<String> month;
	
	@FXML
	private ComboBox<String> year;
	
	@FXML 
    private TableView<StudentInformation> studentTable;

    @FXML 
    private TableColumn<StudentInformation, String> firstNameColumn;
	
	@FXML 
    private TableColumn<StudentInformation, String> lastNameColumn;
	
	@FXML 
    private TableColumn<StudentInformation, String> midNameColumn;
	
	@FXML 
    private TableColumn<StudentInformation, String> studentIdColumn;
	
	@FXML 
    private TableColumn<StudentInformation, Integer> gradeColumn;
	
    @FXML 
    private TableColumn<StudentInformation, Double> hoursColumn;
	
	@FXML
	private Label status;
	
	private ObservableList<StudentInformation> tableItems;
	
	private MongoDatabase communityServiceDB;
	
	private MongoCollection<Document> collection;
	
	private MongoCollection<Document> studentInfoCollection;
	
	
	@FXML
	void search(ActionEvent event){
		tableItems.clear();
		status.setText("");
		int monthOfYear = monthToNum(month.getValue());
		int yearNumber = Integer.parseInt(year.getValue());
		FindIterable<Document> students = studentInfoCollection.find();
		for(Document student : students){
			double hours = 0;
			FindIterable<Document> docs = collection.find( and( eq( "studentId", student.getString("studentID") ), 
				eq("month", monthOfYear), eq("year", yearNumber) ) );
			if(docs != null){
				for(Document doc : docs)
					hours += doc.getDouble("hours");
				if(student.getString("midName") != null)
					tableItems.add( new StudentInformation(student.getString("firstName"), student.getString("midName"), 
									student.getString("lastName"), student.getString("studentID"), student.getInteger("grade"), hours) );
				else
					tableItems.add( new StudentInformation(student.getString("firstName"), student.getString("lastName"),
									student.getString("studentID"), student.getInteger("grade"), hours) );
				studentTable.setItems(tableItems);
			}
		}
	}
	
	@FXML
	void print(ActionEvent event){
		try {
            String fileName = "reports/Monthly_Student_Reports/Student Hours- " + month.getValue() + ", " + year.getValue() + ".txt";
			FileWriter writer = new FileWriter(fileName, false);
            //increase font here
			writer.write(String.format("                   Hours per Student" + "%n"));
			String formatStr = "%-15s %-20s %-20s %-20s %-10s %-15s%n";
			//increase font here
			writer.write(String.format(formatStr, "Student ID", "First Name", "Middle Name", "Last Name", "Grade", "Hours"));
			//take data from database and add it to the text file
			if(tableItems != null){
				for (StudentInformation student : tableItems){
					if(student.getMidName() != null)
						writer.write( String.format( formatStr, student.getStudentId(), student.getFirstName(), student.getMidName(), 
									student.getLastName(), "" + student.getGrade(), "" + student.getHours() ) );
					else
						writer.write( String.format( formatStr, student.getStudentId(), student.getFirstName(), " ", 
									student.getLastName(), "" + student.getGrade(), "" + student.getHours() ) );
				}
			}
			writer.close();
			status.setText("File Downloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		PropertyValueFactory<StudentInformation, String> firstNameProperty = 
          new PropertyValueFactory<StudentInformation, String>("firstName");
		PropertyValueFactory<StudentInformation, String> lastNameProperty = 
          new PropertyValueFactory<StudentInformation, String>("lastName");
		PropertyValueFactory<StudentInformation, String> midNameProperty = 
          new PropertyValueFactory<StudentInformation, String>("midName");
		PropertyValueFactory<StudentInformation, String> studentIdProperty = 
          new PropertyValueFactory<StudentInformation, String>("studentId");
		PropertyValueFactory<StudentInformation, Integer> gradeProperty = 
          new PropertyValueFactory<StudentInformation, Integer>("grade");
		PropertyValueFactory<StudentInformation, Double> hoursProperty = 
          new PropertyValueFactory<StudentInformation, Double>("hours");
		
		firstNameColumn.setCellValueFactory(firstNameProperty);
		lastNameColumn.setCellValueFactory(lastNameProperty);
		midNameColumn.setCellValueFactory(midNameProperty);
		studentIdColumn.setCellValueFactory(studentIdProperty);
		gradeColumn.setCellValueFactory(gradeProperty);
		hoursColumn.setCellValueFactory(hoursProperty);
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		//MongoCredential credential = new MongoCredential.createCredential("sampleuser");
		communityServiceDB = mongo.getDatabase("communityServiceDB");
		//System.out.println("Connected to the database successfully");
		studentInfoCollection = communityServiceDB.getCollection("studentinfo");
		collection = communityServiceDB.getCollection("studenthours");
		tableItems = FXCollections.observableArrayList();
		LocalDate date = LocalDate.now();
		int monthOfYear = date.getMonthValue();
		int yearNumber = date.getYear();
		FindIterable<Document> students = studentInfoCollection.find();
		for(Document student : students){
			double hours = 0;
			FindIterable<Document> docs = collection.find( and( eq( "studentId", student.getString("studentID") ), 
				eq("month", monthOfYear), eq("year", yearNumber) ) );
			if(docs != null){
				for(Document doc : docs)
					hours += doc.getDouble("hours");
				if(student.getString("midName") != null)
					tableItems.add( new StudentInformation(student.getString("firstName"), student.getString("midName"), 
									student.getString("lastName"), student.getString("studentID"), student.getInteger("grade"), hours) );
				else
					tableItems.add( new StudentInformation(student.getString("firstName"), student.getString("lastName"),
									student.getString("studentID"), student.getInteger("grade"), hours) );
				studentTable.setItems(tableItems);
			}
		}
		
		String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", 
								"August", "September", "October", "November", "December"};
		ArrayList<String> months = new ArrayList<String>();
		for(String m : monthNames)
			months.add(m);
		ObservableList<String> monthItems = FXCollections.observableArrayList(months);
		month.setItems(monthItems);
		month.setValue(numToMonth(monthOfYear));
		
		String[] years = {"2015", "2016", "2017", "2018", "2019", "2020"};
		ArrayList<String> yearNumbers = new ArrayList<String>();
		for( String y : years)
			yearNumbers.add(y);
		ObservableList<String> yearItems = FXCollections.observableArrayList(yearNumbers);
		year.setItems(yearItems);
		year.setValue("" + yearNumber);
	}
	
	private int monthToNum(String month){
		if(month.equals("January")) return 1;
		if(month.equals("February")) return 2;
		if(month.equals("March")) return 3;
		if(month.equals("April")) return 4;
		if(month.equals("May")) return 5;
		if(month.equals("June")) return 6;
		if(month.equals("July")) return 7;
		if(month.equals("August")) return 8;
		if(month.equals("September")) return 9;
		if(month.equals("October")) return 10;
		if(month.equals("November")) return 11;
		if(month.equals("December")) return 12;
		return 0;
	}
	
	private String numToMonth(int monthNum){
		if(monthNum == 1) return "January";
		if(monthNum == 2) return "February";
		if(monthNum == 3) return "March";
		if(monthNum == 4) return "April";
		if(monthNum == 5) return "May";
		if(monthNum == 6) return "June";
		if(monthNum == 7) return "July";
		if(monthNum == 8) return "August";
		if(monthNum == 9) return "September";
		if(monthNum == 10) return "October";
		if(monthNum == 11) return "November";
		if(monthNum == 12) return "December";
		return "";
	}
}