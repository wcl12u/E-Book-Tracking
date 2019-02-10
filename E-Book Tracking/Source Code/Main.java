package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;

public class Main extends Application {

	//Private fields hold one state + root layout of main window
	private Stage primaryStage;

	//Start
	@Override
	public void start(Stage primaryStage) {
		try {

			//Set main application + title + icon
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("APCS-A Intro to JavaFX E-Book Tracking");

			//Set application icon
			this.primaryStage.getIcons().add(new Image("/application/AppIcon.png"));

			//Initialize rootlayout
			initRootLayout();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//launch the application
	public static void main(String[] args) {
		launch(args);
	}

	//initialize rootlayout
	public void initRootLayout() {
		try {

			//main initialization
			TabPane rootLayout;

			//create FXML loader
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("RootLayout.fxml"));

			//set the rootlayout field
			rootLayout = (TabPane) loader.load();

			//set scene object to rootLayout
			Scene editRecord = new Scene(rootLayout);

			//give controller access
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			//set the primarystage
			primaryStage.setScene(editRecord);

			//display
			primaryStage.show();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}