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
    private JFXComboBox<String> cmbPositionName;

    @FXML
    private TextField txtId;

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

        initializeValidation();
    }

    private void initializeValidation() {
        addValidationListener(txtId, "E[1-9]+", true); // Upper 'E' followed by numbers (1-9) only
        addValidationListener(txtNic, "[0-9V]+", true); // Numbers and 'V' (upper case)
        addValidationListener(txtName, "[a-zA-Z]+", true); // Letters only
        addValidationListener(txtSalary, "\\d+", true); // Numbers only
        addValidationListener(txtAddress, "[a-zA-Z ]+", true); // Letters and space only
        addValidationListener(txtTel, "\\d{10}", true); // Exactly 10 numbers
        addValidationListener(txtStatus, "(Active|Inactive)", true); // 'Active' or 'Inactive' words only
    }

    private void addValidationListener(TextField textField, String regex, boolean caseSensitive) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(regex)) {
                // If input matches the regex pattern
                textField.setStyle("-fx-border-color: #3498db;");
            } else {
                // If input doesn't match the regex pattern
                textField.setStyle("-fx-border-color: red;");
            }
        });

        if (!caseSensitive) {
            textField.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().replaceAll(regex, ""));
                return change;
            }));
        }
    }











    private void showSelectedEmployeeDetails() {
        EmployeeTm selectedUser = tblEmployee.getSelectionModel().getSelectedItem();
        tblEmployee.setOnMouseClicked(event -> showSelectedEmployeeDetails());
        if (selectedUser != null) {
            txtId.setText(selectedUser.getEmployeeID());
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
            txtId.setText(nextEmployeeId);

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


    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        Stage stage = (Stage) rootNode.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
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
        txtId.setText("");
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

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtId.getText();
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
            txtId.setText(employee.getEmployeeID());
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
        String id = txtId.getText();
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
    }
}
