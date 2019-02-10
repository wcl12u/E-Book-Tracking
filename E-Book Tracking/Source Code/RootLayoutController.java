package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RootLayoutController {

	private Main mainApp;

	//Fields

	@FXML
	private AnchorPane root;
	@FXML
	private Tab tabCodeEdit;
	@FXML
	private Tab tabStudentEdit;
	@FXML
	private Tab tabReport;
	@FXML
	private Button btnPreviousCode;
	@FXML
	private Button btnNewCode;
	@FXML
	private Button btnDeleteCode;
	@FXML
	private Button btnSaveCode;
	@FXML
	private Button btnSaveEditCode;
	@FXML
	private Button btnNextCode;
	@FXML
	private TextField txtCode;
	@FXML
	private TextField txtCodeAssign;
	@FXML
	private Button btnCodeAssign;
	@FXML
	private Button btnCodeUnassign;
	@FXML
	private TextField txtStudentID;
	@FXML
	private TextField txtFirstName;
	@FXML
	private TextField txtLastName;
	@FXML
	private ChoiceBox<String> chbxGrade;
	@FXML
	private Button btnPreviousStudent;
	@FXML
	private Button btnNewStudent;
	@FXML
	private Button btnDeleteStudent;
	@FXML
	private Button btnSaveStudent;
	@FXML
	private Button btnSaveEditStudent;
	@FXML
	private Button btnNextStudent;
	@FXML
	private Button btnPreviousView;
	@FXML
	private Button btnNextView;
	@FXML
	private ListView<CellLayoutReport> listReport;
	@FXML
	private VBox vboxAssign;
	@FXML
	private RadioButton radbtnCode;
	@FXML
	private RadioButton radbtnID;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnAll;
	@FXML
	private HBox hboxSearch;
	@FXML
	private HBox hboxReportInc;

	private boolean searching = false;
	private boolean reportClick = true;
	private int codeIndex = -1;
	private int studentIndex = -1;
	private int searchIndex = 0;
	private int showIndex = 0;
	private String search;

	ObservableList<String> grades = FXCollections.observableArrayList("9", "10", "11", "12");

	ObservableList<Student> studentList = FXCollections.observableArrayList();

	ObservableList<CellLayoutReport> codeList = FXCollections.observableArrayList();

	ObservableList<CellLayoutReport> searchList = FXCollections.observableArrayList();

	ObservableList<CellLayoutReport> showList = FXCollections.observableArrayList();

	//Method Called on Program Startup

	public void initialize() {
		
		listReport.widthProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				handleReportView();
			}
		});
		listReport.heightProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				handleReportView();
			}
		});
		
		readStudent();
		readCodes();

		root.getChildren();

		chbxGrade.setItems(grades);
		listReport.setItems(codeList);

		radbtnCode.setSelected(true);

		if(studentList.size() == 0) {
			handleNewStudent();
		}
		else {
			studentIndex = 0;
			handleStudentView();
		}
		if(codeList.size() != 0) {
			codeIndex = 0;
		}

	}

	//View set when student edit page is selected

	public void handleStudentView() {
		if(tabStudentEdit.isSelected()) {
			reportClick = true;
			if(studentIndex != -1) {
				Student student = studentList.get(studentIndex);
				String redeemCode = checkStudentCode(student.getStudentID());
				if(!redeemCode.equals("-1")) {
					txtCodeAssign.setText(redeemCode);
					txtCodeAssign.setDisable(true);
					btnCodeUnassign.setDisable(false);
					btnCodeAssign.setDisable(true);
					txtStudentID.setText("Cannot Edit Student ID While Redemption Code is Assigned");
					txtStudentID.setDisable(true);
				}
				else {
					txtCodeAssign.setText("");
					txtCodeAssign.setDisable(false);
					btnCodeAssign.setDisable(false);
					btnCodeUnassign.setDisable(true);
					txtStudentID.setDisable(false);
					txtStudentID.setText(student.getStudentID());
				}
				txtFirstName.setText(student.getFirstName());
				txtLastName.setText(student.getLastName());
				chbxGrade.setValue(String.valueOf(student.getGrade()));
				btnSaveEditStudent.setDisable(false);
				btnSaveStudent.setDisable(true);
				btnDeleteStudent.setDisable(false);
				vboxAssign.setDisable(false);
			}
			else {
				handleNewStudent();
			}
		}
		else if(tabCodeEdit.isSelected()) {
			handleCodeView();
		}
		else if(tabReport.isSelected()) {
			handleReportView();
		}
		handleIncrementDisable();
	}

	//View set when redemption code edit page is selected

	public void handleCodeView() {
		if(tabCodeEdit.isSelected()) {
			reportClick = true;
			if(codeIndex != -1) {
				CellLayoutReport temp = codeList.get(codeIndex);
				RedemptionCode redeem = temp.getRedeem();
				txtCode.setText(redeem.getCode());
				if(redeem.getStudentID().equals("-1")) {
					btnSaveEditCode.setDisable(false);
					btnSaveEditCode.setText("Save Edit");
				}
				else {
					btnSaveEditCode.setText("No Editing\nWhen Assign");
					btnSaveEditCode.setDisable(true);
				}
				btnSaveCode.setDisable(true);
				btnDeleteCode.setDisable(false);
			}
			else {
				handleNewCode();
			}
		}
		else if(tabReport.isSelected()) {
			handleReportView();
		}
		handleIncrementDisable();
	}

	//View set when report view page is selected

	public void handleReportView() {
		if(tabReport.isSelected()) {
			hboxSearch.setMaxWidth(root.getWidth()*0.66);
			hboxSearch.setMinWidth(root.getWidth()*0.66);
			hboxReportInc.setMaxWidth(root.getWidth()*0.33);
			hboxReportInc.setMinWidth(root.getWidth()*0.33);
			if(searching != true) {
				if(reportClick == true) {
					if(radbtnID.isSelected()) {
						if(studentIndex != -1) {
							showIndex = studentIndex;
						}
						handleSearchByStudent();
					}
					if(radbtnCode.isSelected()) {
						if(codeIndex != -1) {
							showIndex = codeIndex;
						}
						handleSearchByCode();
					}
					reportClick = false;
				}
				listReport.setItems(showList);
				txtSearch.clear();
				for(CellLayoutReport element: showList) {
					element.setView(listReport, studentList);
				}
				handleIncrementDisable();
				listReport.scrollTo(showIndex);
			}
			else {
				listReport.setItems(searchList);
				txtSearch.setText(search);
				for(CellLayoutReport element: searchList) {
					element.setView(listReport, studentList);
				}
				handleIncrementDisable();
				listReport.scrollTo(searchIndex);
			}
		}
	}

	//Handle redemption code assignment button
	public void handleCodeAssign() {
		boolean filled = true;
		boolean usable = false;
		boolean assigned = false;
		int index = -1;
		if(stringSafetyCheck(txtCodeAssign)) {
			for(CellLayoutReport element: codeList) {
				if(element.getCode().equals(txtCodeAssign.getText())) {
					if(element.getStudentID().equals("-1"))	{
						usable = true;
						index = codeList.indexOf(element);
					}
					else {
						assigned = true;
					}
				}
			}
		}
		else {
			filled = false;
		}
		if(!usable) {
			if(assigned) {
				txtCodeAssign.clear();
				txtCodeAssign.setPromptText("Code Is Already Assigned");
			}
			else {
				txtCodeAssign.clear();
				txtCodeAssign.setPromptText("Code Does Not Exist");
			}
		}

		if(filled && usable) {
			RedemptionCode code = new RedemptionCode();
			code.setCode(txtCodeAssign.getText());
			txtCodeAssign.setPromptText(null);
			code.setStudentID(txtStudentID.getText());
			CellLayoutReport temp = new CellLayoutReport(code);
			codeList.set(index, temp);
			writeCodes();
			handleStudentView();
		}
	}

	//Handle redemption code unassignment button
	public void handleCodeUnassign() {
		int index = -1;
		for(CellLayoutReport element: codeList) {
			if(element.getCode().equals(txtCodeAssign.getText())) {
				index = codeList.indexOf(element);
			}
		}
		RedemptionCode code = new RedemptionCode();
		code.setCode(txtCodeAssign.getText());
		txtCodeAssign.setPromptText(null);
		code.setStudentID("-1");
		CellLayoutReport temp = new CellLayoutReport(code);
		codeList.set(index, temp);
		writeCodes();
		handleStudentView();
	}

	public void handleSearchByStudent() {
		showList.clear();
		for(Student element: studentList) {
			String code = checkStudentCode(element.getStudentID());
			if(!code.equals("-1")) {
				RedemptionCode code2 = new RedemptionCode(code);
				code2.setStudentID(element.getStudentID());
				CellLayoutReport temp = new CellLayoutReport(code2);
				showList.add(temp);
			}
			else {
				RedemptionCode code2 = new RedemptionCode();
				code2.setStudentID(element.getStudentID());
				CellLayoutReport temp = new CellLayoutReport(code2);
				showList.add(temp);
			}
		}
		if(showList.size() != 0 && !showList.get(showIndex).getCode().equals("None")) {
			for(CellLayoutReport element: codeList) {
				if(element.getCode().equals(showList.get(showIndex).getCode())) {
					codeIndex = codeList.indexOf(element);
					break;
				}
			}
		}
		if(showList.size() != 0) {
			studentIndex = showIndex;
		}
		else if(showList.size() == 0) {
			RedemptionCode temp = new RedemptionCode();
			CellLayoutReport temp2 = new CellLayoutReport(temp);
			showList.add(temp2);
		}
		if(!reportClick) {
			handleReportView();
		}
	}
	
	public void handleSearchByCode() {
		showList.clear();
		for(CellLayoutReport element: codeList) {
			showList.add(element);
		}
		if(showList.size() != 0 && !showList.get(showIndex).getStudentID().equals("No Student Assigned")) {
			for(Student element: studentList) {
				if(element.getStudentID().equals(showList.get(showIndex).getStudentID())) {
					studentIndex = studentList.indexOf(element);
					break;
				}
			}
		}
		if(showList.size() != 0) {
			codeIndex = showIndex;
		}
		else if(showList.size() == 0) {
			RedemptionCode temp = new RedemptionCode();
			CellLayoutReport temp2 = new CellLayoutReport(temp);
			showList.add(temp2);
		}
		if(!reportClick) {
			handleReportView();
		}
	}


	//Handle Search Button
	public void handleSearch() {
		boolean filled = true;
		if(stringSafetyCheck(txtSearch)) {
			search = txtSearch.getText();
			searching = true;
			if(radbtnCode.isSelected()) {
				for(CellLayoutReport element: codeList) {
					if(element.getCode().startsWith(search)) {
						searchList.add(element);
					}
				}
			}
			else if (radbtnID.isSelected()){
				for(Student element: studentList) {
					if(element.getStudentID().startsWith(search)) {
						String code = checkStudentCode(element.getStudentID());
						if(!code.equals("-1")) {
							RedemptionCode code2 = new RedemptionCode(code);
							code2.setStudentID(element.getStudentID());
							CellLayoutReport temp = new CellLayoutReport(code2);
							searchList.add(temp);
						}
						else {
							RedemptionCode code2 = new RedemptionCode();
							code2.setStudentID(element.getStudentID());
							CellLayoutReport temp = new CellLayoutReport(code2);
							searchList.add(temp);
						}
					}
				}
				/* for(CellLayoutReport element: codeList) {
					if(element.getStudentID().startsWith(search)) {
						searchList.add(element);
					}
				} */
			}
		}
		else {
			filled = false;
		}
		if(filled) {
			btnSearch.setDisable(true);
			if(searchList.size() != 0 && !searchList.get(searchIndex).getCode().equals("None")) {
				for(CellLayoutReport element: codeList) {
					if(element.getCode().equals(searchList.get(searchIndex).getCode())) {
						codeIndex = codeList.indexOf(element);
						break;
					}
				}
			}
			else if(searchList.size() == 0){
				RedemptionCode temp = new RedemptionCode("None");
				CellLayoutReport temp2 = new CellLayoutReport(temp);
				searchList.add(temp2);
			}
			handleReportView();
		}
	}

	//Handle new student button
	public void handleNewStudent() {
		btnDeleteStudent.setDisable(true);
		btnSaveEditStudent.setDisable(true);
		btnSaveStudent.setDisable(false);
		txtCodeAssign.clear();
		txtCodeAssign.setPromptText(null);
		vboxAssign.setDisable(true);
		txtFirstName.clear();
		txtLastName.clear();
		txtStudentID.clear();
		txtFirstName.setPromptText(null);
		txtLastName.setPromptText(null);
		txtStudentID.setPromptText(null);
		txtStudentID.setDisable(false);
	}

	//Handle new redemption code button
	public void handleNewCode() {
		btnDeleteCode.setDisable(true);
		btnSaveEditCode.setText("Save Edit");
		btnSaveEditCode.setDisable(true);
		btnSaveCode.setDisable(false);
		txtCode.clear();
		txtCode.setPromptText(null);
	}

	//Handle next student button
	public void handleNextStudent() {
		studentIndex++;
		handleStudentView();
	}

	//Handle previous student button
	public void handlePreviousStudent() {
		studentIndex--;
		handleStudentView();
	}

	//Handle next redemption code button
	public void handleNextCode() {
		if(searching) {
			handleShowAll();
		}
		codeIndex++;
		handleCodeView();
	}

	//Handle previous redemption code button
	public void handlePreviousCode() {
		if(searching) {
			handleShowAll();
		}
		codeIndex--;
		handleCodeView();
	}

	//Handle next report button
	public void handleNextReport() {
		if(searching) {
			searchIndex++;
			if(!searchList.get(searchIndex).getCode().equals("None")) {
				codeIndex = codeList.indexOf(searchList.get(searchIndex));
			}
		}
		else {
			showIndex++;
			if(!showList.get(showIndex).getCode().equals("None")) {
				for(CellLayoutReport element: codeList) {
					if(element.getCode().equals(showList.get(showIndex).getCode())) {
						codeIndex = codeList.indexOf(element);
						break;
					}
				}
			}
			if(!showList.get(showIndex).getStudentID().equals("No Student Assigned")) {
				for(Student element: studentList) {
					if(element.getStudentID().equals(showList.get(showIndex).getStudentID())) {
						studentIndex = studentList.indexOf(element);
						break;
					}
				}
			}
		}
		handleReportView();
	}

	//Handle previous report button
	public void handlePreviousReport() {
		if(searching) {
			searchIndex--;
			if(!searchList.get(searchIndex).getCode().equals("None")) {
				codeIndex = codeList.indexOf(searchList.get(searchIndex));
			}
		}
		else {
			showIndex--;
			if(!showList.get(showIndex).getCode().equals("None")) {
				for(CellLayoutReport element: codeList) {
					if(element.getCode().equals(showList.get(showIndex).getCode())) {
						codeIndex = codeList.indexOf(element);
						break;
					}
				}
			}
			if(!showList.get(showIndex).getStudentID().equals("No Student Assigned")) {
				for(Student element: studentList) {
					if(element.getStudentID().equals(showList.get(showIndex).getStudentID())) {
						studentIndex = studentList.indexOf(element);
						break;
					}
				}
			}
		}
		handleReportView();
	}

	//Handle show all button
	public void handleShowAll() {
		searching = false;
		if(searchList.size() != 0 && !searchList.get(searchIndex).getCode().equals("None")) {
			codeIndex = codeList.indexOf(searchList.get(searchIndex));
		}
		searchIndex = 0;
		searchList.clear();
		btnSearch.setDisable(false);
		handleReportView();
	}

	//Handle Save Student
	public void handleSaveStudent() {
		boolean filled = true;
		Student student = new Student();

		if (stringSafetyCheck(txtFirstName) == true) {
			student.setFirstName(txtFirstName.getText());
			txtFirstName.setPromptText(null);
		}
		else {
			filled = false;
		}

		if(stringSafetyCheck(txtLastName) == true) {
			student.setLastName(txtLastName.getText());
			txtLastName.setPromptText(null);
		}
		else {
			filled = false;
		}

		if(stringSafetyCheck(txtStudentID) == true) {
			student.setStudentID(txtStudentID.getText());
			txtStudentID.setPromptText(null);
		}
		else {
			filled = false;
		}

		if(choiceSafetyCheck(chbxGrade) == true) {
			student.setGrade(Integer.valueOf(chbxGrade.getValue()));
		}
		else {
			filled = false;
		}

		if(filled != false) {
			studentList.add(student);
			writeStudent();
			btnSaveStudent.setDisable(true);
			btnSaveEditStudent.setDisable(false);
			btnDeleteStudent.setDisable(false);
			vboxAssign.setDisable(false);
			btnCodeAssign.setDisable(false);
			btnCodeUnassign.setDisable(true);
			studentIndex = studentList.size()-1;
			handleIncrementDisable();
		}
	}

	//Handle edit student
	public void handleEditStudent() {
		boolean filled = true;
		Student student = new Student();

		if (stringSafetyCheck(txtFirstName) == true) {
			student.setFirstName(txtFirstName.getText());
			txtFirstName.setPromptText(null);
		}
		else {
			filled = false;
		}

		if(stringSafetyCheck(txtLastName) == true) {
			student.setLastName(txtLastName.getText());
			txtLastName.setPromptText(null);
		}
		else {
			filled = false;
		}

		if(stringSafetyCheck(txtStudentID) == true) {
			student.setStudentID(txtStudentID.getText());
			txtStudentID.setPromptText(null);
		}
		else {
			filled = false;
		}

		if(choiceSafetyCheck(chbxGrade) == true) {
			student.setGrade(Integer.valueOf(chbxGrade.getValue()));
		}
		else {
			filled = false;
		}

		if(filled != false) {
			studentList.set(studentIndex, student);
			writeStudent();
		}
	}

	//Handle Delete Student
	public void handleDeleteStudent() {
		if(!btnCodeUnassign.isDisable()) {
			handleCodeUnassign();
		}
		studentList.remove(studentIndex);
		writeStudent();
		if(studentList.size() != 0 && studentIndex-1 == -1) {
			studentIndex = 0;
		}
		else {
			studentIndex--;
		}
		handleStudentView();
	}

	//Handle Delete Code
	public void handleDeleteCode() {
		codeList.remove(codeIndex);
		writeCodes();
		if(codeList.size() != 0 && codeIndex-1 == -1) {
			codeIndex = 0;
		}
		else {
			codeIndex--;
		}
		handleCodeView();
	}

	//Handle Save Redemption Code
	public void handleSaveRedemptionCode() {

		boolean filled = true;
		RedemptionCode redeem = new RedemptionCode();

		if(stringSafetyCheck(txtCode) == true) {
			redeem.setCode(txtCode.getText());
			txtCode.setPromptText("");
		}
		else {
			filled = false;
		}

		if(filled != false) {
			CellLayoutReport temp = new CellLayoutReport(redeem);
			codeList.add(temp);
			writeCodes();
			btnSaveCode.setDisable(true);
			btnSaveEditCode.setDisable(false);
			btnDeleteCode.setDisable(false);
			codeIndex = codeList.size()-1;
			handleIncrementDisable();
		}
	}

	//Handle Save Edit Redemption Code
	public void handleEditRedemptionCode() {
		boolean filled = true;
		RedemptionCode redeem = new RedemptionCode();

		if(stringSafetyCheck(txtCode) == true) {
			redeem.setCode(txtCode.getText());
			txtCode.setPromptText(null);
		}
		else {
			filled = false;
		}

		if(filled != false) {
			CellLayoutReport temp = new CellLayoutReport(redeem);
			codeList.set(codeIndex, temp);
			writeCodes();
		}
	}

	//Checks if redemption code is assigned
	public String checkStudentCode(String studentID) {
		String returnCode = "-1";
		for(CellLayoutReport element: codeList) {
			if(element.getStudentID().equals(studentID)) {
				return element.getCode();
			}
		}
		return returnCode;
	}

	//Safety check for a choice box field
	public boolean choiceSafetyCheck(ChoiceBox<String> choiceBox) {
		boolean safe = false;
		boolean temp;
		try {
			temp = choiceBox.getValue().isEmpty();
			if(temp == false) {
				safe = true;
			}
			return safe;
		} catch (Exception e) {
			safe = false;
			return safe;
		}
	}

	//Safety check for string fields of a text field
	public boolean stringSafetyCheck(TextField txtField) {
		boolean safe;
		if (txtField.getText().isEmpty() == false) {
			safe = true;
			return safe;
		} 
		else {
			txtField.clear();
			txtField.setPromptText("Enter a value");
			safe = false;
			return safe;
		} 
	}

	//Safety check for integer fields
	public boolean integerSafetyCheck(TextField txtField) {
		boolean safe;
		Integer temp;
		try {
			temp = Integer.valueOf(txtField.getText());
			temp += 1;
			safe = true;
			return safe;
		} catch (Exception e) {
			txtField.clear();
			txtField.setPromptText("Enter a value");
			safe = false;
			return safe;
		} 
	}

	//Check if long values are valid
	public boolean longSafetyCheck(TextField txtField) {
		boolean safe;
		Long temp;
		try {
			temp = Long.valueOf(txtField.getText());
			temp += 1;
			safe = true;
			return safe;
		} catch (Exception e) {
			txtField.clear();
			txtField.setPromptText("Enter a value");
			safe = false;
			return safe;
		} 
	}

	public void handleIncrementDisable() {
		if (tabStudentEdit.isSelected() == true) {
			if (studentIndex+1 >= studentList.size()) {
				btnNextStudent.setDisable(true);
			}
			else {
				btnNextStudent.setDisable(false);
			}
			if (studentIndex-1 <= -1) {
				btnPreviousStudent.setDisable(true);
			}
			else {
				btnPreviousStudent.setDisable(false);
			}
		}
		else if (tabCodeEdit.isSelected() == true) {
			if (codeIndex-1 <= -1) {
				btnPreviousCode.setDisable(true);
			}
			else {
				btnPreviousCode.setDisable(false);
			}
			if (codeIndex+1 >= codeList.size()) {
				btnNextCode.setDisable(true);
			}
			else {
				btnNextCode.setDisable(false);
			}
		}
		else if (tabReport.isSelected()) {
			if(!searching) {
				if (showIndex-1 <= -1) {
					btnPreviousView.setDisable(true);
				}
				else {
					btnPreviousView.setDisable(false);
				}
				if(showIndex+1 >= showList.size()) {
					btnNextView.setDisable(true);
				}
				else {
					btnNextView.setDisable(false);
				}
			}
			else {
				if (showIndex-1 <= -1) {
					btnPreviousView.setDisable(true);
				}
				else {
					btnPreviousView.setDisable(false);
				}
				if(showIndex+1 >= showList.size()) {
					btnNextView.setDisable(true);
				}
				else {
					btnNextView.setDisable(false);
				}
			}
		}
	}

	public void writeStudent() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("students");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			for(Student element: studentList) {
				objectOutputStream.writeObject(element);
			}
			FileOutputStream fileOutputStream2 = new FileOutputStream(new File("studentlen"));
			fileOutputStream2.write(studentList.size());
			fileOutputStream2.close();
			fileOutputStream.close();
			objectOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeCodes() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("codes");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			for(CellLayoutReport element: codeList) {
				objectOutputStream.writeObject(element.getRedeem());
			}
			FileOutputStream fileOutputStream2 = new FileOutputStream(new File("codelen"));
			fileOutputStream2.write(codeList.size());
			fileOutputStream2.close();
			fileOutputStream.close();
			objectOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readStudent() {
		try {
			if(new File("students").exists()) {
				FileInputStream fileInputStream = new FileInputStream("students");
				if(fileInputStream.available() > 0) {
					ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
					FileInputStream fileInputStream2 = new FileInputStream("studentlen");
					int len = fileInputStream2.read();
					fileInputStream2.close();
					for(int x=0; x < len; x++) {
						Student student = (Student) objectInputStream.readObject();
						studentList.add(student);
					}
					fileInputStream.close();
					objectInputStream.close();
				}
			}
			else {
				new File("students").createNewFile();
				new File("studentlen").createNewFile();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void readCodes() {
		try {
			if(new File("codes").exists()) {
				FileInputStream fileInputStream = new FileInputStream("codes");
				if(fileInputStream.available() > 0) {
					ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
					FileInputStream fileInputStream2 = new FileInputStream("codelen");
					int len = fileInputStream2.read();
					fileInputStream2.close();
					for(int x = 0; x < len; x++) {
						RedemptionCode code = (RedemptionCode) objectInputStream.readObject();
						CellLayoutReport temp = new CellLayoutReport(code);
						codeList.add(temp);
					}
					fileInputStream.close();
					objectInputStream.close();
				}
			}
			else {
				new File("codes").createNewFile();
				new File("codelen").createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}
}