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

public class weeklyCSAController implements Initializable {
	
	@FXML
	private ComboBox<String> week;
	
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
	
	private int weekOfYear;
	
	private int yearNumber;
	
	
	@FXML
	void search(ActionEvent event){
		tableItems.clear();
		status.setText("");
		int chosenWeek = Integer.parseInt(week.getValue());
		int chosenYear = Integer.parseInt(year.getValue());
		FindIterable<Document> programNames = csaCollection.find();
		for(Document name : programNames){
			double hours = 0;
			FindIterable<Document> docs = collection.find( and( eq( "programName", name.getString("programName") ), 
				eq("week", chosenWeek), eq("year", chosenYear) ) );
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
            String fileName = "reports/Weekly_CSA_Reports/CSA Report- Week " + week.getValue() + ", " + year.getValue() + ".txt";
			FileWriter writer = new FileWriter(fileName, false);
            //increase font here
			writer.write(String.format("   CSA Categories and Hours  Week " + week.getValue() + ", " + year.getValue() + "%n"));
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
		Locale locale = Locale.US;
		weekOfYear = date.get(WeekFields.of(locale).weekOfWeekBasedYear());
		yearNumber = date.getYear();
		FindIterable<Document> programNames = csaCollection.find();
		for(Document name : programNames){
			double hours = 0;
			FindIterable<Document> docs = collection.find( and( eq( "programName", name.getString("programName") ), 
				eq("week", weekOfYear), eq("year", yearNumber) ) );
			if(docs != null){
				for(Document doc : docs)
					hours += doc.getDouble("hours");
				tableItems.add( new csaProgramAndHours(name.getString("programName"), hours) );
				csaTable.setItems(tableItems);
			}
		}
		
		ArrayList<String> weeks = new ArrayList<String>();
		for(int i = 1; i <= 52; i++)
			weeks.add("" + i);
		ObservableList<String> weekItems = FXCollections.observableArrayList(weeks);
		week.setItems(weekItems);
		week.setValue("" + weekOfYear);
		
		String[] years = {"2015", "2016", "2017", "2018", "2019", "2020"};
		ArrayList<String> yearNumbers = new ArrayList<String>();
		for( String y : years)
			yearNumbers.add(y);
		ObservableList<String> yearItems = FXCollections.observableArrayList(yearNumbers);
		year.setItems(yearItems);
		year.setValue("" + yearNumber);
	}
}