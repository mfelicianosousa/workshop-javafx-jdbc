package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity;
	
	private SellerService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML 
	private TextField txtId;
	
	@FXML 
	private TextField txtName;
	
	@FXML 
	private TextField txtEmail;
	
	@FXML 
	private DatePicker dtpBirthDate;
	
	@FXML 
	private TextField txtBaseSalary;
	
	@FXML 
	private Label lblErrorName;
	
	@FXML 
	private Label lblErrorEmail;
	
	@FXML 
	private Label lblErrorBirthDate;
	
	@FXML 
	private Label lblErrorBaseSalary;
	
	@FXML 
	private Button btnSave;
	
	@FXML 
	private Button btnCancel;
	
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
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
		    // notificação
		    notifyDataChangeListeners();
		    
		    // CloseWindow
		    Utils.currentStage(event).close();
		} catch (ValidationException e) {
				
			setErrorMessages(e.getErrors());
				
		} catch (DbException e) {
			
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		} 
	}
	
	private void notifyDataChangeListeners() {
		
		for (DataChangeListener listener : dataChangeListeners ) {
			listener.onDataChanged();
		}
		
	}

	/* Method responsible for reading the editText and instantiating a department object*/
	private Seller getFormData() {
		
		Seller entity = new Seller();
		ValidationException exception = new ValidationException("Validation error");
		
		
		entity.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addErrors("name", "Field can't be empty");
		}
		entity.setName(txtName.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception ;
		}

		return entity;
	}

	
	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		 // CloseWindow
	    Utils.currentStage(event).close();
	}
	
	// Update Seller 
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	// SellerService 
	public void setSellerService(SellerService service) {
	   this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();	
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.SetTextFieldMaxLength(txtName, 50);
		Constraints.SetTextFieldMaxLength(txtEmail, 60);
		Constraints.SetTextFieldDouble(txtBaseSalary);
		Utils.formatDatePicker(dtpBirthDate, "dd/MM/yyyy");
	}
	

	public void updateFormData() {
		if (entity == null ) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText( String.format("%.2f",entity.getBaseSalary()));
		txtName.setText(entity.getName());
		if (entity.getBirthDate() != null) {
		    dtpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(),ZoneId.systemDefault()));
		}
	}

	private void setErrorMessages( Map<String,String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			lblErrorName.setText(errors.get("name"));
		}
	}
}
