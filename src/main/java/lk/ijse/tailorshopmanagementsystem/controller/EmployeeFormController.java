package lk.ijse.tailorshopmanagementsystem.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.tailorshopmanagementsystem.Util.Regex;
import lk.ijse.tailorshopmanagementsystem.model.Employee;
import lk.ijse.tailorshopmanagementsystem.model.tm.EmployeeTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.ProductTm;
import lk.ijse.tailorshopmanagementsystem.repository.EmployeeRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EmployeeFormController {

    public AnchorPane rootNode;
    public JFXButton btnSave;
    public Label txtUserId;
    public TextField txtStatus;

    @FXML
    private Label lblEmployeeId;

    @FXML
    private JFXComboBox<String> cmbPositionName;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtSalary;

    @FXML
    private TableColumn<EmployeeTm, String> colId;

    @FXML
    private TableColumn<EmployeeTm, String> colNic;

    @FXML
    private TableColumn<EmployeeTm, String> colPosition;

    @FXML
    private TableColumn<EmployeeTm, String> colName;

    @FXML
    private TableColumn<EmployeeTm, String>colTel;

    @FXML
    private TableColumn<EmployeeTm, String> colAddress;

    @FXML
    private TableColumn<EmployeeTm, String> colSalary;

    @FXML
    private TableColumn<EmployeeTm, String> colStatus;

    @FXML
    private TableColumn<EmployeeTm, String> colUserId;

    @FXML
    private TableView<EmployeeTm> tblEmployee;



    public void initialize() {
        getCurrentEmployeeId();
        getEmployeePosition();
        setCellValueFactory();
        loadAllEmployees();
        txtUserId.setText("U01");
        txtStatus.setText("Active");
        showSelectedEmployeeDetails();
    }

    public void telKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.TEL, txtTel);
    }

    public void nicKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NIC, txtNic);
    }

    public void nameKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
    }

    public void addressKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.ADDRESS, txtAddress);
    }

    public void salaryKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.PRICEDOT, txtSalary);
    }

    public void statusKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.STATUS, txtStatus);
    }

    public boolean isValied(){
        boolean nameValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
        boolean nicValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NIC, txtNic);
        boolean addressValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.ADDRESS, txtAddress);
        boolean telValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.TEL, txtTel);
        boolean salary = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.PRICEDOT, txtSalary);
        boolean statusValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.STATUS, txtStatus);

        return nameValid && nicValid && addressValid && telValid && statusValid && salary;
    }

    private void showSelectedEmployeeDetails() {
        EmployeeTm selectedUser = tblEmployee.getSelectionModel().getSelectedItem();
        tblEmployee.setOnMouseClicked(event -> showSelectedEmployeeDetails());
        if (selectedUser != null) {
            lblEmployeeId.setText(selectedUser.getEmployeeID());
            txtNic.setText(selectedUser.getNIC());
            txtName.setText(selectedUser.getEmployeeName());
            cmbPositionName.setValue(selectedUser.getPosition());
            txtSalary.setText(selectedUser.getSalary());
            txtAddress.setText(selectedUser.getEmployeeAddress());
            txtTel.setText(selectedUser.getPhoneNumber());
            txtUserId.setText(selectedUser.getUserID());
            txtStatus.setText(selectedUser.getStatus());
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("employeeAddress"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAllEmployees() {
        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<Employee> employeeList = EmployeeRepo.getAll();
            for (Employee employee : employeeList) {
                EmployeeTm tm = new EmployeeTm(
                        employee.getEmployeeID(),
                        employee.getUserID(),
                        employee.getNIC(),
                        employee.getPosition(),
                        employee.getEmployeeName(),
                        employee.getPhoneNumber(),
                        employee.getEmployeeAddress(),
                        employee.getSalary(),
                        employee.getStatus()
                );

                obList.add(tm);
            }

            tblEmployee.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentEmployeeId() {
        try {
            String currentId = EmployeeRepo.getEmpId();

            String nextEmployeeId = generateNextEmployeeId(currentId);
            lblEmployeeId.setText(nextEmployeeId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextEmployeeId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("E");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "E" + ++idNum;
        }
        return "O1";
    }

    private List<String> getEmployeePosition() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> codeList = EmployeeRepo.getPosition();

            for (String code : codeList) {
                obList.add(code);
            }
            cmbPositionName.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obList;
    }

    public void cmbPositionOnAction(ActionEvent actionEvent) {
        String position = cmbPositionName.getValue();
        try {
            String salary = EmployeeRepo.searchByPosition(position);

            txtSalary.setText(salary);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    private void clearFields() {
        lblEmployeeId.setText("");
        txtAddress.setText("");
        txtNic.setText("");
        txtTel.setText("");
        txtName.setText("");
        cmbPositionName.setValue(null);
        txtSalary.setText("");

        getCurrentEmployeeId();
        getEmployeePosition();
        setCellValueFactory();
        loadAllEmployees();
        txtUserId.setText("U01");
        txtStatus.setText("Active");

        txtNic.setStyle("");
        txtName.setStyle("");
        txtSalary.setStyle("");
        txtAddress.setStyle("");
        txtTel.setStyle("");
        txtStatus.setStyle("");

    }
    @FXML
    void btnSaveOnAction(ActionEvent event) {

        if (isValied() && (cmbPositionName.getValue() != null && !cmbPositionName.getValue().toString().isEmpty())) {

            String id = lblEmployeeId.getText();
            String nic = txtNic.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String tel = txtTel.getText();
            String salary = txtSalary.getText();
            String position = cmbPositionName.getValue();
            String userId = txtUserId.getText();
            String status = "Active";

            Employee employee = new Employee(id, userId, nic, position, name, tel, address, salary, status);

            try {
                boolean isSaved = EmployeeRepo.save(employee);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "customer saved!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Show error message if validation fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please fill in all fields correctly.");
            alert.showAndWait();
        }
    }
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String nic = txtNic.getText();

        try {
            boolean isDeleted = EmployeeRepo.delete(nic);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee deleted!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String nic = txtNic.getText();

        Employee employee = EmployeeRepo.nicSearch(nic);
        if (employee != null) {
            lblEmployeeId.setText(employee.getEmployeeID());
            txtNic.setText(employee.getNIC());
            txtName.setText(employee.getEmployeeName());
            cmbPositionName.setValue(employee.getPosition());
            txtSalary.setText(employee.getSalary());
            txtAddress.setText(employee.getEmployeeAddress());
            txtTel.setText(employee.getPhoneNumber());
            txtUserId.setText(employee.getUserID());
            txtStatus.setText(employee.getStatus());
        }else {
            new Alert(Alert.AlertType.INFORMATION, "employee not found!").show();
        }
    }

    public void btnActiveOnAction(ActionEvent actionEvent) {
        txtStatus.setText("Active");
    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        if (isValied() && (cmbPositionName.getValue() != null && !cmbPositionName.getValue().toString().isEmpty())) {

            String id = lblEmployeeId.getText();
            String nic = txtNic.getText();
            String name = txtName.getText();
            String position = cmbPositionName.getValue();
            String salary = txtSalary.getText();
            String address = txtAddress.getText();
            String tel = txtTel.getText();
            String userId = "U01";
            String status = txtStatus.getText();

            Employee employee = new Employee(id, userId, nic, position, name, tel, address, salary, status);

            try {
                boolean isUpdated = EmployeeRepo.update(employee);
                if(isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "employee updated!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        } else {
            // Show error message if validation fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please fill in all fields correctly.");
            alert.showAndWait();
        }
    }
}
