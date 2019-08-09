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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department department;
	private DepartmentService depService;

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErrorName;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;

	public void setDeparment(Department department) {
		this.department = department;
	}

	public void setDepartmentService(DepartmentService service) {
		this.depService = service;
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (department == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (depService == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			department = getFormData();
			depService.saveOrUpdate(department);
			Utils.currentStage(event).close();

		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private Department getFormData() {
		Department depart = new Department();

		depart.setId(Utils.tryParseToInt(txtId.getText()));
		depart.setName(txtName.getText());

		return depart;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void updateFormData() {
		if (department == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(department.getId()));
		txtName.setText(department.getName());
	}
}
