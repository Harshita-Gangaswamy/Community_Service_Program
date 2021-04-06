package commTracker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class LoginController implements Initializable {
    
	@FXML
	private Label correction;
	
	@FXML 
    private TextField username;
	
	@FXML 
    private TextField password;
	
	@FXML 
    void userLogin(ActionEvent event) {
		if(username.getText().equals("admin") && password.getText().equals("1234")){
			try {
			  URL menuBarUrl = getClass().getResource("Menubar.fxml");
			  URL paneUrl = getClass().getResource("searchStudent.fxml");
			  MenuBar bar = FXMLLoader.load( menuBarUrl );
			  AnchorPane anchorPane = FXMLLoader.load( paneUrl );
			  BorderPane border = MainApp.getRoot();
			  border.setTop(bar); 			  
			  border.setCenter(anchorPane);		  
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			correction.setText("Your username or/and password is incorrect");
	}
	
	@FXML 
    void clearLogin(ActionEvent event) {
		username.setText("");
		password.setText("");
		correction.setText("");
	}
    
	@Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }
}
