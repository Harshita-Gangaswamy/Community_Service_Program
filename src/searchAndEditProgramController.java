package commTracker;

import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;

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
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class searchAndEditProgramController implements Initializable {
    
	@FXML
	private Label correction;
	
	@FXML
	private Label status;
	
	@FXML
	private TextField programId;
	
	@FXML
	private TextField programName;
	
	@FXML
	private TextArea description;
	
	@FXML
	private Button editBtn;
	
	@FXML
	private Button saveBtn;
	
	@FXML
	private Button deleteBtn;
	
	private String originalDescription;
	
	private String originalName;
	
	private String originalID;
	
	
	@FXML
	void search(ActionEvent event){
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase communityServiceDB = mongo.getDatabase("communityServiceDB");
		MongoCollection<Document> collection = communityServiceDB.getCollection("csaprograms");
		Document doc = collection.find( eq( "programId", programId.getText() ) ).first();
		if(doc != null){
			programName.setText(doc.getString("programName"));
			description.setText(doc.getString("description"));
			originalDescription = doc.getString("description");
			originalName = doc.getString("programName");
			originalID = programId.getText();
			editBtn.setVisible(true);
			editBtn.setDisable(false);
		} else 
			correction.setText("That program does not exist");
	}
	
	@FXML
	void edit(ActionEvent event){
		programName.setEditable(true);
		description.setEditable(true);
		
		saveBtn.setVisible(true);
		saveBtn.setDisable(false);
		
		deleteBtn.setVisible(true);
		deleteBtn.setDisable(false);
	}
	
	@FXML
	void save(ActionEvent event){
		if( !originalID.equals(programId.getText()) )
			correction.setText("You can not change the ID");
		else if( !programName.getText().equals(originalName) || 
			!description.getText().equals(originalDescription) ){
			
			MongoClient mongo = new MongoClient("localhost", 27017);
			MongoDatabase communityServiceDB = mongo.getDatabase("communityServiceDB");
			MongoCollection<Document> collection = communityServiceDB.getCollection("csaprograms");
			if( !programName.getText().equals(originalName) )
				collection.updateOne( eq("programId", originalID), Updates.set("programName", programName.getText() ) );
			
			if( !description.getText().equals(originalDescription) )
				collection.updateOne( eq("programId", originalID), Updates.set("description", description.getText() ) );
			
			status.setText("Program information updated successfully");
			correction.setText("");
		} else 			
			correction.setText("Nothing has been changed in order to save");
	}
	
	@FXML
	void delete(ActionEvent event){
		Stage confirmation = new Stage();
		confirmation.setTitle("Confirm");
		
		confirmation.setWidth(300);
		confirmation.setHeight(200);
		
		confirmation.initModality(Modality.APPLICATION_MODAL);
		confirmation.initStyle(StageStyle.UTILITY);
		confirmation.toFront();
		
		Pane root = new Pane();
		
		Label clarification = new Label();
		clarification.setText("Are you sure that you want to delete " + programName.getText() + "?");
		clarification.setWrapText(true);
		
		Button yes = new Button("YES");
		yes.setLayoutX(60);
		yes.setLayoutY(30);
		yes.setOnAction(e -> { 
			MongoClient mongo = new MongoClient("localhost", 27017);
			MongoDatabase communityServiceDB = mongo.getDatabase("communityServiceDB");
			MongoCollection<Document> collection = communityServiceDB.getCollection("csaprograms");
			collection.deleteOne( eq("programId", originalID) );
			confirmation.close();
			clearFields();
		});
		Button no = new Button("NO");
		no.setLayoutX(10);
		no.setLayoutY(30);
		no.setOnAction(e -> {
			confirmation.close();
		});
		root.getChildren().addAll(clarification, no, yes);
		
		Scene scene = new Scene(root);
		confirmation.setScene(scene);
		confirmation.show();
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {		
		editBtn.setVisible(false);
		editBtn.setDisable(true);
		description.setWrapText(true);
		disable();
    }
	
	private void disable(){
		programName.setEditable(false);
		description.setEditable(false);
		
		saveBtn.setVisible(false);
		saveBtn.setDisable(true);
		
		deleteBtn.setVisible(false);
		deleteBtn.setDisable(true);
	}
	
	private void clearFields(){
		programId.clear();
		programName.clear();
		description.clear();
	}	
}