package commTracker;

import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;

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

public class monthlyCSAController implements Initializable {
	
	@FXML
	private ComboBox<String> month;
	
	@FXML
	private ComboBox<String> year;
	
	@FXML 
    private TableView<csaProgramAndHours> csaTable;

    @FXML 
    private TableColumn<csaProgramAndHours, String> categoryColumn;

    @FXML 
    private TableColumn<csaProgramAndHours, Double> hoursColumn;
	
	@FXML
	private Label status;
	
	private ObservableList<csaProgramAndHours> tableItems;
	
	private MongoDatabase communityServiceDB;
	
	private MongoCollection<Document> collection;
	
	private MongoCollection<Document> csaCollection;
	
	
	@FXML
	void search(ActionEvent event){
		tableItems.clear();
		status.setText("");
		int monthOfYear = monthToNum(month.getValue());
		int yearNumber = Integer.parseInt(year.getValue());
		FindIterable<Document> programNames = csaCollection.find();
		for(Document name : programNames){
			double hours = 0;
			FindIterable<Document> docs = collection.find( and( eq( "programName", name.getString("programName") ), 
				eq("month", monthOfYear), eq("year", yearNumber) ) );
			if(docs != null){
				for(Document doc : docs)
					hours += doc.getDouble("hours");
				tableItems.add( new csaProgramAndHours(name.getString("programName"), hours) );
				csaTable.setItems(tableItems);
			}
		}
	}
	
	@FXML
	void print(ActionEvent event){
		try {
            String fileName = "reports/Monthly_CSA_Reports/CSA Report- " + month.getValue() + ", " + year.getValue() + ".txt";
			FileWriter writer = new FileWriter(fileName, false);
            //increase font here
			writer.write(String.format("   CSA Categories and Hours " + month.getValue() + ", " + year.getValue() + "%n"));
			String formatStr = "%-25s %-15s%n";
			//increase font here
			writer.write(String.format(formatStr, "Category", "Hours"));
			//take data from database and add it to the text file
			if(tableItems != null){
				for (csaProgramAndHours program : tableItems){
					writer.write( String.format( formatStr, program.getCategory(), "" + program.getHours() ) );
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
		PropertyValueFactory<csaProgramAndHours, String> categoryProperty = 
          new PropertyValueFactory<csaProgramAndHours, String>("category");
		PropertyValueFactory<csaProgramAndHours, Double> hoursProperty = 
          new PropertyValueFactory<csaProgramAndHours, Double>("hours");
		
		categoryColumn.setCellValueFactory(categoryProperty);
		hoursColumn.setCellValueFactory(hoursProperty);
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		communityServiceDB = mongo.getDatabase("communityServiceDB");
		csaCollection = communityServiceDB.getCollection("csaprograms");
		collection = communityServiceDB.getCollection("studenthours");
		tableItems = FXCollections.observableArrayList();
		LocalDate date = LocalDate.now();
		int monthOfYear = date.getMonthValue();
		int yearNumber = date.getYear();
		FindIterable<Document> programNames = csaCollection.find();
		for(Document name : programNames){
			double hours = 0;
			FindIterable<Document> docs = collection.find( and( eq( "programName", name.getString("programName") ), 
				eq("month", monthOfYear), eq("year", yearNumber) ) );
			if(docs != null){
				for(Document doc : docs)
					hours += doc.getDouble("hours");
				tableItems.add( new csaProgramAndHours(name.getString("programName"), hours) );
				csaTable.setItems(tableItems);
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