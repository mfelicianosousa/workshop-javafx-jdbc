package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DepartmentFormController implements Initializable {
	
	@FXML 
	private TextField txtId;
	
	@FXML 
	private TextField txtName;
	
	@FXML 
	private Label lblErrorName;
	
	@FXML 
	private Button btnSave;
	
	@FXML 
	private Button btnCancel;
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		System.out.println("onBtnSaveAction");
	}
	
	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		System.out.println("onBtnCancelAction");
	}

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.SetTextFieldMaxLength(txtName, 30);
		
		
	}

}
