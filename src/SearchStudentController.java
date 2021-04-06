package commTracker;

import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;

import com.mongodb.client.FindIterable; 
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import static com.mongodb.client.model.Filters.*;
import java.util.Iterator; 
import org.bson.Document;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class SearchStudentController implements Initializable {
    
	@FXML
	private Label correction;
	
	@FXML
	private TextField studentID;
	
	@FXML
	void search(ActionEvent event){
		if(studentID.getText().length() == 7){
			MongoClient mongo = new MongoClient("localhost", 27017);
			MongoDatabase communityServiceDB = mongo.getDatabase("communityServiceDB");
			MongoCollection<Document> collection = communityServiceDB.getCollection("studentinfo");
			Document info = collection.find( eq( "studentID", studentID.getText() ) ).first();
			if(info != null){
				StudentInfoController.setStudentInfo(info);
				try {
				  URL menuBarUrl = getClass().getResource("Menubar.fxml");
				  URL paneUrl = getClass().getResource("studentInfo.fxml");
				  MenuBar bar = FXMLLoader.load( menuBarUrl );
				  AnchorPane anchorPane = FXMLLoader.load( paneUrl );
				  BorderPane border = MainApp.getRoot();
				  border.setTop(bar); 			  
				  border.setCenter(anchorPane);		  
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				correction.setText("Invalid ID");
		} else
			correction.setText("Invalid ID");
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }
}
