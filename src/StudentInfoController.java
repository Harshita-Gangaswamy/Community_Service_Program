package commTracker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import com.mongodb.client.FindIterable; 
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.Updates;
import java.util.Iterator; 
import org.bson.Document; 

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class StudentInfoController implements Initializable {
    
	private static Document studentInfo;
	
	private ToggleGroup group1;
	
	@FXML
	private Label correction;
	
	@FXML
	private Label editingStatus;
	
	@FXML
	private TextField studentID;
	
	@FXML
	private TextField firstName;
	
	@FXML
	private TextField lastName;
	
	@FXML
	private TextField midName;
	
	@FXML
	private RadioButton grade9;
	
	@FXML
	private RadioButton grade10;
	
	@FXML
	private RadioButton grade11;
	
	@FXML
	private RadioButton grade12;
	
	@FXML
	private Button saveBtn;
	
	private int[] grades = {9, 10, 11, 12};
	
	
	public static void setStudentInfo(Document info){ studentInfo = info;	}
	
	@FXML
	void edit(ActionEvent event){
		enable();
		showInfo();
		editingStatus.setText("");
		correction.setText("");
		saveBtn.setDisable(false);
	}
	
	@FXML
	void save(ActionEvent event) {
		if(firstName.getText().equals("") || lastName.getText().equals("") 
			|| studentID.getText().length() != 7 || group1.getSelectedToggle() == null ){
			
			correction.setText("1 or more fields have not been filled");
			
		} else if (!firstName.getText().equals(studentInfo.getString("firstName")) ||
				!lastName.getText().equals(studentInfo.getString("firstName")) ||
				!studentID.getText().equals(studentInfo.getString("studentID")) ||
				!group1.getSelectedToggle().toString().contains("" + studentInfo.getInteger("grade")) || 
				(studentInfo.getString("midName") == null && !midName.getText().equals("")) ||
				(studentInfo.getString("midName") != null && !midName.getText().equals(studentInfo.getString("midName")) ) ){
			
			MongoClient mongo = new MongoClient("localhost", 27017);
			MongoDatabase communityServiceDB = mongo.getDatabase("communityServiceDB");
			MongoCollection<Document> collection = communityServiceDB.getCollection("studentinfo");
			if( !studentID.getText().equals(studentInfo.getString("studentID")) )
				collection.updateOne( eq("studentID", studentInfo.getString("studentID")), Updates.set("studentID", studentID.getText() ) );
			
			if( !firstName.getText().equals(studentInfo.getString("firstName")) )
				collection.updateOne( eq("studentID", studentID.getText()), Updates.set("firstName", firstName.getText() ) );
			
			if( !lastName.getText().equals(studentInfo.getString("lastName")) )
				collection.updateOne( eq("studentID", studentID.getText()), Updates.set("lastName", lastName.getText() ) );
			
			if( studentInfo.getString("midName") == null && !midName.getText().equals("") ){
				Document doc = collection.find( eq("studentID", studentID.getText())).first();
				doc.append("midName", midName.getText());
			} else if( studentInfo.getString("midName") != null && !midName.getText().equals(studentInfo.getString("midName")) )
				collection.updateOne( eq("studentID", studentID.getText()), Updates.set("midName", midName.getText() ) );
			if( !group1.getSelectedToggle().toString().contains("" + studentInfo.getInteger("grade")) )
				collection.updateOne( eq("studentID", studentID.getText()), Updates.set("grade", gradeFinder() ) );
			
			disable();
			editingStatus.setText("Student information updated successfully");
			correction.setText("");
		} else 
			correction.setText("Nothing has been changed in order to save");
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        group1 = new ToggleGroup();
		grade9.setToggleGroup(group1);
		grade10.setToggleGroup(group1);
		grade11.setToggleGroup(group1);
		grade12.setToggleGroup(group1);
		
		showInfo();
		disable();
    }
	
	private void showInfo(){
		firstName.setText(studentInfo.getString("firstName"));
		
		lastName.setText(studentInfo.getString("lastName"));
		
		studentID.setText(studentInfo.getString("studentID"));
		
		if(studentInfo.getInteger("grade") == 9)
			grade9.setSelected(true);
		else if(studentInfo.getInteger("grade") == 10)
			grade10.setSelected(true);
		else if(studentInfo.getInteger("grade") == 11)
			grade11.setSelected(true);
		else
			grade12.setSelected(true);
		
		if(studentInfo.getString("midName") != null)
			midName.setText(studentInfo.getString("midName"));
	}
	
	private void disable(){
		saveBtn.setVisible(false);
		saveBtn.setDisable(true);
		firstName.setEditable(false);
		lastName.setEditable(false);
		studentID.setEditable(false);
		midName.setEditable(false);
		grade9.setDisable(true);
		grade10.setDisable(true);
		grade11.setDisable(true);
		grade12.setDisable(true);
	}
	
	private void enable(){
		firstName.setEditable(true); 
		lastName.setEditable(true);
		midName.setEditable(true);
		studentID.setEditable(true);
		grade9.setDisable(false);
		grade10.setDisable(false);
		grade11.setDisable(false);
		grade12.setDisable(false);
		saveBtn.setVisible(true);
	}
	
	private int gradeFinder(){
		String str = group1.getSelectedToggle().toString();
		for(int grade : grades){
			if(str.contains("" + grade))
				return grade;
		}
		return 0;
	}
}
