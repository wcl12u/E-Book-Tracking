package application;

import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class CellLayoutReport extends Pane {

	private CellLayoutReportController controller;
	private RedemptionCode redeem;
	private String code, studentID;

	public CellLayoutReport(RedemptionCode redeem) {
		Node view = null;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CellLayoutReport.fxml"));
		fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> param) {
				return controller = new CellLayoutReportController();
			}
		});
		try {
			view = (Node) fxmlLoader.load();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		getChildren().add(view);
		this.redeem = redeem;
		code = redeem.getCode();
		studentID = redeem.getStudentID();
	}

	public CellLayoutReport() {
		Node view = null;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CellLayoutReport.fxml"));
		fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> param) {
				return controller = new CellLayoutReportController();
			}
		});
		try {
			view = (Node) fxmlLoader.load();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		getChildren().add(view);
	}

	public RedemptionCode getRedeem() {
		return redeem;
	}

	public String getCode() {
		return code;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setView(ListView<CellLayoutReport> listView, ObservableList<Student> studentList) {
		if(listView.getWidth() >= 400) {
			controller.splitpane.setMaxWidth(listView.getWidth()-16.0);
			controller.splitpane.setMinWidth(listView.getWidth()-16.0);
			controller.hboxReport.setMaxWidth(listView.getWidth()-16.0);
			controller.hboxReport.setMinWidth(listView.getWidth()-16.0);
			double size = ((((listView.getWidth()-600.0)/100.0)+55.8)/4.65);
			double size2 = ((((listView.getWidth()-600.0)/100.0)+7.0)*(12.0/7.0))+4.0;
			Font fontSize = new Font(size);
			controller.lblFirstName.setFont(fontSize);
			controller.lblLastName.setFont(fontSize);
			controller.lblStudentID.setFont(fontSize);
			controller.lblGrade.setFont(fontSize);
			controller.lblCode.setFont(fontSize);
			controller.lblFirstNameHead.setFont(fontSize);
			controller.lblLastNameHead.setFont(fontSize);
			controller.lblStudentIDHead.setFont(fontSize);
			controller.lblGradeHead.setFont(fontSize);
			controller.lblCodeHead.setFont(fontSize);
			controller.lblStudentHead.setStyle("-fx-font-weight: bold; -fx-font-size: " + size2 + ";");
			controller.lblRedemHead.setStyle("-fx-font-weight: bold; -fx-font-size: " + size2 + ";");
		}
		else {
			controller.hboxReport.setMaxWidth(384);
			controller.hboxReport.setMinWidth(384);
		}
		if(listView.getHeight() >= 312) {
			controller.hboxReport.setMaxHeight(listView.getHeight()-5.0);
			controller.hboxReport.setMinHeight(listView.getHeight()-5.0);
			double size3 = (listView.getHeight()/listView.getWidth())*25.0;
			if(size3 <= 13.0) {
				controller.vboxCodeView.setStyle("-fx-spacing: " + size3 + ";");
				controller.vboxStudentInfo.setStyle("-fx-spacing: " + size3 + ";");
			}
		}
		else {
			controller.hboxReport.setMaxHeight(307);
			controller.hboxReport.setMinHeight(307);
		}
		controller.ancCodeView.maxWidthProperty().bind(controller.splitpane.widthProperty().multiply(0.3));
		controller.ancCodeView.minWidthProperty().bind(controller.splitpane.widthProperty().multiply(0.3));
		controller.ancStudentInfo.maxWidthProperty().bind(controller.splitpane.widthProperty().multiply(0.7));
		controller.ancStudentInfo.minWidthProperty().bind(controller.splitpane.widthProperty().multiply(0.7));
		controller.lblCode.setText(code);
		int index = isAssigned(studentList);
		if(index != -1) {
			Student student = studentList.get(index);
			controller.lblFirstName.setText(student.getFirstName());
			controller.lblLastName.setText(student.getLastName());
			controller.lblStudentID.setText(studentID);
			controller.lblGrade.setText(String.valueOf(student.getGrade()));
		}
		else {
			controller.lblFirstName.setText("No Student Assigned");
			controller.lblLastName.setText("No Student Assigned");
			controller.lblStudentID.setText("No Student Assigned");
			controller.lblGrade.setText("No Student Assigned");
		}


	}

	public int isAssigned(ObservableList<Student> studentList) {
		for(Student element: studentList) {
			if(studentID.equals(element.getStudentID())) {
				return studentList.indexOf(element);
			}
		}
		return -1;
	}

}
