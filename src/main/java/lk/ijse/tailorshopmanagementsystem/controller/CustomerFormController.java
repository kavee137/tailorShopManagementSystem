package lk.ijse.tailorshopmanagementsystem.controller;

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
import lk.ijse.tailorshopmanagementsystem.model.Customer;
import lk.ijse.tailorshopmanagementsystem.model.Customer1;
import lk.ijse.tailorshopmanagementsystem.model.tm.CustomerTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.UserTm;
import lk.ijse.tailorshopmanagementsystem.repository.CustomerRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CustomerFormController {
    public AnchorPane rootNode;
    @FXML
    private JFXComboBox<String> cmbStatus;
    @FXML
    private Label lblCustomerId;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private TableColumn<?, ?> colNic;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    public void initialize() {
        setCellValueFactory();
        loadAllCustomers();
        getCurrentCustomerId();
        showSelectedUserDetails();
        setTxtStatus();
    }

    private void showSelectedUserDetails() {
        CustomerTm selectedUser = tblCustomer.getSelectionModel().getSelectedItem();
        tblCustomer.setOnMouseClicked(event -> showSelectedUserDetails());
        if (selectedUser != null) {
            lblCustomerId.setText(selectedUser.getCustomerID());
            txtName.setText(selectedUser.getCustomerName());
            txtNic.setText(selectedUser.getNIC());
            txtAddress.setText(selectedUser.getCustomerAddress());
            txtTel.setText(selectedUser.getCustomerTel());
            cmbStatus.setValue(selectedUser.getStatus());
        }
    }

    private void getCurrentCustomerId() {
        try {
            String currentId = CustomerRepo.getCurrentId();

            String nextCustomerId = generateNextCustomerId(currentId);
            lblCustomerId.setText(nextCustomerId);
            cmbStatus.setValue("Active");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextCustomerId(String currentId) {
        if(currentId != null && !currentId.isEmpty()) {
            String[] split = currentId.split("C");
            if (split.length > 1) {
                int idNum = Integer.parseInt(split[1]);
                return "C0" + String.format("%02d", ++idNum);
            }
        }
        return "C01"; // Default starting ID
    }

    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("customerTel"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAllCustomers() {
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for (Customer customer : customerList) {
                CustomerTm tm = new CustomerTm(
                        customer.getCustomerID(),
                        customer.getNIC(),
                        customer.getCustomerName(),
                        customer.getCustomerAddress(),
                        customer.getCustomerTel(),
                        customer.getUserID(),
                        customer.getStatus()
                );
//                System.out.println(tm);
                obList.add(tm);
            }

            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = lblCustomerId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();
        String status = cmbStatus.getValue();
        String userId = "U01";

        if (isValied()) {
            Customer customer = new Customer(id, nic, name, address, tel, userId, status);

            try {
                boolean isUpdated = CustomerRepo.update(customer);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
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

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblCustomerId.getText();
        String nic = txtNic.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();
        String userId = "U01";
        String status = "Active";

        Customer customer = new Customer(id, nic, name, address, tel, userId, status);
        if (isValied()) {
            try {
                boolean isSaved = CustomerRepo.save(customer);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "customer saved!").show();
                    clearFields();
//                getCurrentCustomerId();
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

    public void nameKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
    }

    public void nicKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NIC, txtNic);
    }

    public void addressKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.ADDRESS, txtAddress);
    }

    public void telKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.TEL, txtTel);
    }


//    public void idKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
//        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.CUSID, lblCustomerId);
//    }

    public boolean isValied(){
        boolean nameValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
        boolean nicValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NIC, txtNic);
        boolean addressValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.ADDRESS, txtAddress);
        boolean telValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.TEL, txtTel);
//        boolean idValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.CUSID, txtCustomerId);

        return nameValid && nicValid && addressValid && telValid;
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String nic = txtNic.getText();

        if (isSearchNicValied()) {

            Customer1 customer1 = CustomerRepo.nicSearch(nic);
            if (customer1 != null) {
                lblCustomerId.setText(customer1.getCustomerId());
                txtName.setText(customer1.getCustomerName());
                txtAddress.setText(customer1.getCustomerAddress());
                txtTel.setText(customer1.getCustomerTel());
                cmbStatus.setValue(customer1.getStatus());
            } else {
                new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
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

    public boolean isSearchNicValied(){
        boolean nicValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NIC, txtNic);
        return nicValid;
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String nic = txtNic.getText();

        try {
            boolean isDeleted = CustomerRepo.delete(nic);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
        getCurrentCustomerId();
    }

    private void clearFields() {
        lblCustomerId.setText("");

        txtNic.setText("");
        txtNic.setStyle(""); // Reset style to default

        txtName.setText("");
        txtName.setStyle(""); // Reset style to default

        txtAddress.setText("");
        txtAddress.setStyle(""); // Reset style to default

        txtTel.setText("");
        txtTel.setStyle(""); // Reset style to default

    }

    @FXML
    public void btnActiveOnAction(ActionEvent actionEvent) {
        cmbStatus.setValue("Active");
    }

    private void setTxtStatus() {
        ObservableList<String> status = FXCollections.observableArrayList();
        cmbStatus.setValue("Active");

        status.add("Active");
        status.add("Inactive");

        cmbStatus.setItems(status);
    }

}