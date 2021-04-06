package commTracker;

import java.io.IOException;
import java.net.URL;

import com.mongodb.client.FindIterable; 
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import static com.mongodb.client.model.Filters.*;
import java.util.Iterator; 
import org.bson.Document;

import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;

public class MenubarController {
	
	@FXML
	private Menu add;

	
	@FXML 
    void editStudentHours(ActionEvent event) {  
		changePage("studentHours.fxml");
    }
	
	@FXML 
    void searchAndEditStudent(ActionEvent event) {  
		changePage("searchStudent.fxml");
    }
	
	@FXML 
    void addStudent(ActionEvent event) {     
		changePage("addStudent.fxml");
    }
	
	@FXML 
    void deleteStudent(ActionEvent event) {      
		changePage("searchStudentDelete.fxml");
    }
	
	@FXML 
    void searchAndEditProgram(ActionEvent event) {  
		changePage("searchAndEditProgram.fxml");
    }
	
	@FXML 
    void addProgram(ActionEvent event) {     
		changePage("addProgram.fxml");
    }
	
	@FXML 
    void generateWeeklyCSA(ActionEvent event) {      
		changePage("weeklyCSA.fxml");
    }
	
	@FXML 
    void generateMonthlyCSA(ActionEvent event) {      
		changePage("monthlyCSA.fxml");
    }
	
	@FXML 
    void generateWeeklyStudent(ActionEvent event) {      
		changePage("weeklyStudentReport.fxml");
    }
	
	@FXML 
    void generateMonthlyStudent(ActionEvent event) {      
		changePage("monthlyStudentReport.fxml");
    }
	
	@FXML 
    void logout(ActionEvent event) {     
		try {
			  URL paneUrl = getClass().getResource("Login.fxml");
			  AnchorPane anchorPane = FXMLLoader.load( paneUrl );
			  BorderPane border = MainApp.getRoot();
			  border.setCenter(anchorPane);		  
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private void changePage(String fxmlFile){
		try {
			  URL menuBarUrl = getClass().getResource("Menubar.fxml");
			  URL paneUrl = getClass().getResource(fxmlFile);
			  MenuBar bar = FXMLLoader.load( menuBarUrl );
			  AnchorPane anchorPane = FXMLLoader.load( paneUrl );
			  BorderPane border = MainApp.getRoot();
			  border.setTop(bar); 
			  border.setCenter(anchorPane);		  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}