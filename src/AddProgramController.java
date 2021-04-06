package commTracker;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Random;

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

public class AddProgramController implements Initializable {
    
	@FXML
	private Label correction;
	
	@FXML
	private Label addingStatus;
	
	@FXML
	private TextField programName;
	
	@FXML
	private TextField programId;
	
	@FXML
	private TextArea description;
	
	
	@FXML
	void add(ActionEvent event){
		if(programName.getText().equals("") || programName.getText().equals("")){
			
			correction.setText("Add program name");
			addingStatus.setText("");
			
		} else {
			
			MongoClient mongo = new MongoClient("localhost", 27017);
			MongoDatabase communityServiceDB = mongo.getDatabase("communityServiceDB");
			MongoCollection<Document> collection = communityServiceDB.getCollection("csaprograms");
			Document info = collection.find( eq( "programId", programId.getText() ) ).first();
			if(info == null){
				Document newDoc = new Document("_id", new ObjectId());
				newDoc.append("programName", programName.getText())
					.append("description", description.getText())
					.append("programId", programId.getText());
				collection.insertOne(newDoc);
				addingStatus.setText("Program information added successfully");
				clearFields();
			} else
				correction.setText("ID already exists");
		}
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		description.setWrapText(true);
    }
	
	private void clearFields(){
		programName.clear();
		programId.clear();
		description.clear();
		correction.setText("");
	}
}
