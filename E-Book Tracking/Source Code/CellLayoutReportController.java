package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CellLayoutReportController implements Initializable{
	
	@FXML
	SplitPane splitpane;
	@FXML
	HBox hboxReport;
	@FXML
	AnchorPane ancCodeView;
	@FXML
	AnchorPane ancStudentInfo;
	@FXML
	VBox vboxCodeView;
	@FXML
	VBox vboxStudentInfo;
	@FXML
	Label lblCode;
	@FXML
	Label lblCodeHead;
	@FXML
	Label lblFirstName;
	@FXML
	Label lblFirstNameHead;
	@FXML
	Label lblLastName;
	@FXML
	Label lblLastNameHead;
	@FXML
	Label lblStudentID;
	@FXML
	Label lblStudentIDHead;
	@FXML
	Label lblGrade;
	@FXML
	Label lblGradeHead;
	@FXML
	Label lblStudentHead;
	@FXML
	Label lblRedemHead;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
