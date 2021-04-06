package commTracker;

import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
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
import org.bson.types.ObjectId;

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

public class StudentHoursController implements Initializable {
	
	@FXML
	private Label correction;
	
	@FXML
	private Label status;
	
	@FXML 
	private TextField studentId;
	
	@FXML
	private Label CSAProgram;
	
	@FXML
	private ListView<String> programList;
	
	@FXML
	private ComboBox<String> programName;
	
	@FXML
	private Label totalHrsText;
	
	@FXML 
	private TextField totalHours;
	
	@FXML 
    private TableView<StudentHoursLog> hoursTable;

    @FXML 
    private TableColumn<StudentHoursLog, String> dateColumn;

    @FXML 
    private TableColumn<StudentHoursLog, Double> hoursColumn;
	
	@FXML
	private Label hoursText;
	
	@FXML 
	private TextField enterHours;
	
	@FXML
	private Label dateText;
	
	@FXML
	private DatePicker datePopup;
	
	@FXML
	private Button addProgramBtn;
	
	@FXML
	private Button doneBtn;
	
	@FXML
	private Button addHoursBtn;
	
	@FXML
	private Button cancelHoursBtn;
	
	@FXML
	private Button editBtn;
	
	@FXML
	private Label hoursEditText;
	
	@FXML 
	private TextField editHours;
	
	@FXML
	private Label dateEditText;
	
	@FXML
	private DatePicker editDate;
	
	@FXML
	private Button saveBtn;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Button deleteBtn;
	
	private ToggleGroup group1;
	
	private String csaName;
	
	private LocalDate originalDate;
	
	private double originalHours;
	
	private double totalHoursNum = 0;

	private MongoDatabase communityServiceDB;
	
	private MongoCollection<Document> collection;
	
	private ObservableList<StudentHoursLog> tableItems;
	
	
	@FXML
	void searchId(ActionEvent event){
		MongoCollection<Document> studentIDs = communityServiceDB.getCollection("studentinfo");
		Document student = studentIDs.find( eq( "studentID", studentId.getText() ) ).first();
		if(student != null){
			CSAProgram.setText("CSA Program Name");
			programName.setVisible(true);
			programName.setDisable(false);
			
			FindIterable<Document> docs = collection.find( eq( "studentId", studentId.getText() ) );
			ArrayList<String> awardsItems = new ArrayList<String>();
			if(docs != null){
				for( Document doc : docs){
					if(awardsItems.contains(doc.getString("programName")) == false)
						awardsItems.add(doc.getString("programName"));
				}
				ObservableList<String> items = FXCollections.observableArrayList(awardsItems);
				programName.setItems(items);
			}
			
			totalHrsText.setText("Total Hours");
			totalHours.setVisible(true);
			totalHours.setText("");
			
			addProgramBtn.setVisible(true);
			addProgramBtn.setDisable(false);
			
		} else 
			correction.setText("Invalid ID");
	}
	
	@FXML
	void searchSpecific(ActionEvent event){
		tableItems.clear();
		csaName = programName.getValue();
		FindIterable<Document> docs = collection.find( and( eq( "studentId", studentId.getText() ), eq( "programName", csaName ) ) );
		totalHoursNum = 0;
		if( docs != null){
			for( Document doc : docs){
				totalHoursNum += doc.getDouble("hours");
				tableItems.add( new StudentHoursLog ( doc.getString("date"), doc.getDouble("hours") ) );
			}
			hoursTable.setItems(tableItems);
			totalHours.setText("" + totalHoursNum);
		}
		hoursTable.setVisible(true);
		hoursTable.setEditable(true);
		
		hoursText.setText("Hours");
		
		enterHours.setVisible(true);
		enterHours.setEditable(true);
		
		dateText.setText("Date");
		
		datePopup.setVisible(true);
		datePopup.setEditable(true);
		
		addHoursBtn.setVisible(true);
		addHoursBtn.setDisable(false);
		
		cancelHoursBtn.setVisible(true);
		cancelHoursBtn.setDisable(false);
		
		editBtn.setVisible(true);
		editBtn.setDisable(false);
		
		deleteBtn.setVisible(true);
		deleteBtn.setDisable(false);
	}
	
	@FXML
	void addProgramName(ActionEvent event){
		programList.setVisible(true);
		programList.setDisable(false);
		
		programName.setVisible(false);
		programName.setDisable(true);
		
		doneBtn.setVisible(true);
		doneBtn.setDisable(false);
		
		MongoCollection<Document> csaProgramCollection = communityServiceDB.getCollection("csaprograms");
		FindIterable<Document> iterDoc = csaProgramCollection.find();
		ArrayList<String> programItems = new ArrayList<String>();
		for(Document doc : iterDoc)
			programItems.add( doc.getString("programName") );
		ObservableList<String> items = FXCollections.observableArrayList(programItems);
		programList.setItems(items);
	}
	
	@FXML
	void done(ActionEvent event){
		ObservableList<String> items = programList.getSelectionModel().getSelectedItems();;
		
		programList.setVisible(false);
		programList.setDisable(true);
		
		programName.setVisible(true);
		programName.setDisable(false);
		
		programName.setItems(items);
		
		doneBtn.setVisible(false);
		doneBtn.setDisable(true);
	}
	
	@FXML
	void addHours(ActionEvent event){
		if(!enterHours.getText().equals("") && datePopup.getValue() != null){
			
			String item = programName.getValue();
			
			LocalDate dateLocal = datePopup.getValue();
			Locale locale = Locale.US;
			int weekOfYear = dateLocal.get(WeekFields.of(locale).weekOfWeekBasedYear());
			int monthOfYear = dateLocal.getMonthValue();
			int year = dateLocal.getYear();
			Document newDoc = new Document("_id", new ObjectId());
			newDoc.append("studentId", studentId.getText())
					.append("hours", Double.parseDouble(enterHours.getText()))
					.append("week", weekOfYear)
					.append("month", monthOfYear)
					.append("year", year)
					.append("date", dateLocal.toString())
					.append("programName", item);
			collection.insertOne(newDoc);
			
			tableItems.add( new StudentHoursLog ( dateLocal.toString(), Double.parseDouble(enterHours.getText()) ) );
			hoursTable.setItems(tableItems);
			
			totalHoursNum += Double.parseDouble(enterHours.getText());
			totalHours.setText("" + totalHoursNum);
			
			datePopup.setValue(null);
			enterHours.clear();
		
			editBtn.setVisible(true);
			editBtn.setDisable(false);
		} else 
			correction.setText("No information was entered");
	}		
	
	@FXML
	void cancelHours(ActionEvent event){
		datePopup.setValue(null);
		enterHours.clear();
		
		cancelHoursBtn.setVisible(false);
		cancelHoursBtn.setDisable(true);
		
		editBtn.setVisible(true);
		editBtn.setDisable(false);		
	}
	
	@FXML
	void edit(ActionEvent event){
		StudentHoursLog volunteerDay = hoursTable.getSelectionModel().getSelectedItem();
		
		hoursEditText.setText("Hours");
		editHours.setVisible(true);
		editHours.setEditable(true);
		
		dateEditText.setText("Date");
		editDate.setVisible(true);
		editDate.setEditable(true);
		
		if(volunteerDay != null){
			editHours.setText("" + volunteerDay.getHours());
			editDate.setValue( LocalDate.parse(volunteerDay.getDate()) );
			
			originalHours = volunteerDay.getHours();
			originalDate = LocalDate.parse(volunteerDay.getDate());
		}
		
		saveBtn.setVisible(true);
		saveBtn.setDisable(false);
		
		cancelBtn.setVisible(true);
		cancelBtn.setDisable(false);
		
		deleteBtn.setVisible(false);
		deleteBtn.setDisable(true);
	}
	
	@FXML
	void save(ActionEvent event){
		if( !editDate.getValue().isEqual(originalDate) || originalHours != Double.parseDouble(editHours.getText()) ){
			LocalDate dateLocal = editDate.getValue();
			if( !editDate.getValue().isEqual(originalDate) ){
				Locale locale = Locale.US;
				int weekOfYear = dateLocal.get(WeekFields.of(locale).weekOfWeekBasedYear());
				int monthOfYear = dateLocal.getMonthValue();
				int year = dateLocal.getYear();
				collection.updateOne( and( eq("studentId", studentId.getText() ), eq("date", originalDate.toString()) ), Updates.set("date", dateLocal.toString() ) );
				collection.updateOne( and( eq("studentId", studentId.getText() ), eq("date", dateLocal.toString()) ), Updates.set("week", weekOfYear) );
				collection.updateOne( and( eq("studentId", studentId.getText() ), eq("date", dateLocal.toString()) ), Updates.set("month", monthOfYear) );
				collection.updateOne( and( eq("studentId", studentId.getText() ), eq("date", dateLocal.toString()) ), Updates.set("year", year) );
			}
			
			if( originalHours != Double.parseDouble(editHours.getText()) ){
				totalHoursNum = totalHoursNum + Double.parseDouble(editHours.getText()) - originalHours;
				totalHours.setText("" + totalHoursNum);
				collection.updateOne( and( eq( "studentId", studentId.getText() ), eq("date", dateLocal.toString()) ), Updates.set("hours", Double.parseDouble(editHours.getText()) ) );
			}
			StudentHoursLog volunteerDay = hoursTable.getSelectionModel().getSelectedItem();
			hoursTable.getItems().remove(volunteerDay);
			tableItems.add( new StudentHoursLog ( dateLocal.toString(), Double.parseDouble(editHours.getText()) ) );
			hoursTable.setItems(tableItems);
			
			hoursEditText.setText("");
			editHours.setVisible(false);
			editHours.setEditable(false);
		
			dateEditText.setText("");
			editDate.setVisible(false);
			editDate.setEditable(false);
			
			saveBtn.setVisible(false);
			saveBtn.setDisable(true);
			
			cancelBtn.setVisible(false);
			cancelBtn.setDisable(true);
			
			deleteBtn.setVisible(true);
			deleteBtn.setDisable(false);
		} else 
			correction.setText("Nothing has been changed in order to be saved");
	}
	
	@FXML
	void cancel(ActionEvent event){
		hoursEditText.setText("");
		editHours.setVisible(false);
		editHours.setEditable(false);
		
		dateEditText.setText("");
		editDate.setVisible(false);
		editDate.setEditable(false);		
		
		saveBtn.setVisible(false);
		saveBtn.setDisable(true);
		
		cancelBtn.setVisible(false);
		cancelBtn.setDisable(true);
		
		deleteBtn.setVisible(true);
		deleteBtn.setDisable(false);
	}
	
	@FXML
	void delete(ActionEvent event){
		StudentHoursLog volunteerDay = hoursTable.getSelectionModel().getSelectedItem();
		totalHoursNum = totalHoursNum - volunteerDay.getHours();
		enterHours.setText("" + totalHoursNum);
		collection.deleteOne( and( eq("studentId", studentId.getText() ), eq("date", volunteerDay.getDate()) ) );
		hoursTable.getItems().remove(volunteerDay);
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {		
		MongoClient mongo = new MongoClient("localhost", 27017);
		communityServiceDB = mongo.getDatabase("communityServiceDB");
		collection = communityServiceDB.getCollection("studenthours");
		
		PropertyValueFactory<StudentHoursLog, String> dateProperty = 
          new PropertyValueFactory<StudentHoursLog, String>("date");
		PropertyValueFactory<StudentHoursLog, Double> hoursProperty = 
          new PropertyValueFactory<StudentHoursLog, Double>("hours");
		
		dateColumn.setCellValueFactory(dateProperty);
		hoursColumn.setCellValueFactory(hoursProperty);
		
		tableItems = FXCollections.observableArrayList();
		
		programList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		datePopup.setShowWeekNumbers(true);
		editDate.setShowWeekNumbers(true);
		
		disable();
	}
	
	private void disable(){
		programName.setVisible(false);
		programName.setDisable(true);
		
		programList.setVisible(false);
		programList.setDisable(true);
		programList.setEditable(false);
		
		totalHours.setVisible(false);
		totalHours.setEditable(false);
		
		addProgramBtn.setVisible(false);
		addProgramBtn.setDisable(true);
		
		doneBtn.setVisible(false);
		doneBtn.setDisable(true);
		
		hoursTable.setVisible(false);
		hoursTable.setEditable(false);
		
		addHoursBtn.setVisible(false);
		addHoursBtn.setDisable(true);
		
		enterHours.setVisible(false);
		enterHours.setEditable(false);
		
		datePopup.setVisible(false);
		datePopup.setEditable(false);
		
		cancelHoursBtn.setVisible(false);
		cancelHoursBtn.setDisable(true);
		
		editBtn.setVisible(false);
		editBtn.setDisable(true);
		
		editHours.setVisible(false);
		editHours.setEditable(false);
		
		editDate.setVisible(false);
		editDate.setEditable(false);
		
		saveBtn.setVisible(false);
		saveBtn.setDisable(true);
		
		cancelBtn.setVisible(false);
		cancelBtn.setDisable(true);
		
		deleteBtn.setVisible(false);
		deleteBtn.setDisable(true);
	}
}