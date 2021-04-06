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
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class DeleteStudentController implements Initializable {
    
	private static Document studentInfo;
	
	private ToggleGroup group1;
	
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
	
	
	public static void setStudentInfo(Document info){ studentInfo = info;	}
	
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
		clarification.setText("Are you sure that you want to delete " + firstName.getText() + " " + lastName.getText() + "?");
		clarification.setWrapText(true);
		
		Button yes = new Button("YES");
		yes.setLayoutX(60);
		yes.setLayoutY(30);
		yes.setOnAction(e -> { 
			MongoClient mongo = new MongoClient("localhost", 27017);
			MongoDatabase communityServiceDB = mongo.getDatabase("communityServiceDB");
			MongoCollection<Document> collection = communityServiceDB.getCollection("studentinfo");
			collection.deleteOne( eq("studentID", studentID.getText()) );
			try {
				  URL menuBarUrl = getClass().getResource("Menubar.fxml");
				  URL paneUrl = getClass().getResource("searchStudentDelete.fxml");
				  MenuBar bar = FXMLLoader.load( menuBarUrl );
				  AnchorPane anchorPane = FXMLLoader.load( paneUrl );
				  BorderPane border = MainApp.getRoot();
				  border.setTop(bar); 			  
				  border.setCenter(anchorPane);		  
			} catch (IOException f) {
				f.printStackTrace();
			}
			confirmation.close();
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
		firstName.setEditable(false);
		lastName.setEditable(false);
		studentID.setEditable(false);
		midName.setEditable(false);
		grade9.setDisable(true);
		grade10.setDisable(true);
		grade11.setDisable(true);
		grade12.setDisable(true);
	}
}
