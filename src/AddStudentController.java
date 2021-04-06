package commTracker;

import java.net.URL;
import java.util.ResourceBundle;

import com.mongodb.client.FindIterable; 
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import static com.mongodb.client.model.Filters.*;
import java.util.Iterator; 
import org.bson.Document;  
import org.bson.types.ObjectId;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class AddStudentController implements Initializable {
    
	@FXML
	private Label correction;
	
	@FXML
	private Label addingStatus;
	
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
	
	ToggleGroup group1;
	
	private int[] grades = {9, 10, 11, 12};
	
	@FXML
	void add(ActionEvent event){
		if(firstName.getText().equals("") || lastName.getText().equals("") 
			|| studentID.getText().length() != 7 || group1.getSelectedToggle() == null ){
			
			correction.setText("1 or more fields have not been filled");
			addingStatus.setText("");
			
		} else {
			
			MongoClient mongo = new MongoClient("localhost", 27017);
			MongoDatabase communityServiceDB = mongo.getDatabase("communityServiceDB");
			MongoCollection<Document> collection = communityServiceDB.getCollection("studentinfo");
			Document info = collection.find( eq( "studentID", studentID.getText() ) ).first();
			if(info != null)
				correction.setText("ID already exists");
			else {
				Document newDoc = new Document("_id", new ObjectId());
				newDoc.append("studentID", studentID.getText())
					.append("lastName", lastName.getText())
					.append("firstName", firstName.getText())
					.append("grade", gradeFinder());
				if(!midName.getText().equals(""))
					newDoc.append("midName", midName.getText());
				collection.insertOne(newDoc);
				addingStatus.setText("Student information added successfully");
				clearFields();
			}
		}
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        group1 = new ToggleGroup();
		grade9.setToggleGroup(group1);
		grade10.setToggleGroup(group1);
		grade11.setToggleGroup(group1);
		grade12.setToggleGroup(group1);
    }
	
	private int gradeFinder(){
		String str = group1.getSelectedToggle().toString();
		for(int i = 0; i < 4; i++){
			if(str.contains("" + grades[i]))
				return grades[i];
		}
		return 0;
	}
	
	private void clearFields(){
		firstName.clear();
		lastName.clear();
		midName.clear();
		studentID.clear();
		correction.setText("");
		grade9.setSelected(false);
		grade10.setSelected(false);
		grade11.setSelected(false);
		grade12.setSelected(false);
	}
}
