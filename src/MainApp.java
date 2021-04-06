package commTracker;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class MainApp extends Application {

	private static BorderPane root = new BorderPane();
	
	public static BorderPane getRoot() {
		return root;
	}
	
    @Override
    public void start(Stage stage) throws Exception {
       		
		URL url = getClass().getResource("Login.fxml");
		AnchorPane pane = FXMLLoader.load( url );
		root.setCenter(pane);		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Volunteer Hours");
		stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
