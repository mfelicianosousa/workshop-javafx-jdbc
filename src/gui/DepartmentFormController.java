package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Department entity;
	
	private DepartmentService service;
	
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
		if (entity == null ) {
			throw new IllegalStateException("Entity was null") ; // Program Defensive, inject Dependency manual
		}
		if (service == null ) {
			throw new IllegalStateException("Service was null") ; // Program Defensive, inject Dependency manual
		}
		try {
		   entity = getFormData();
		   
	    	// save database;
		    service.saveOrUpdate(entity);
		    
		    // CloseWindow
		    Utils.currentStage(event).close();
		    
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	/* Method responsible for reading the editText and instantiating a department object*/
	private Department getFormData() {
		
		Department entity = new Department();
		
		entity.setId(Utils.tryParseToInt(txtId.getText()));
		entity.setName(txtName.getText());

		return entity;
	}

	
	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		 // CloseWindow
	    Utils.currentStage(event).close();
	}
	
	// Update Department 
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	// DepartmentService 
	public void setDepartmentService(DepartmentService service) {
	   this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();	
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.SetTextFieldMaxLength(txtName, 30);
	}
	

	public void updateFormData() {
		if (entity == null ) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

}
