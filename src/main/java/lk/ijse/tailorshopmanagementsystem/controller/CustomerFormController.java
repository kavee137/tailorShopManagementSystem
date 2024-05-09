package lk.ijse.tailorshopmanagementsystem.controller;

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
    private TextField txtStatus;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCustomerId;

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

        initializeValidation();
    }



    //    ------------------------ Validation Part  ---------------------------------

    private void initializeValidation() {
        addValidationListener(txtCustomerId, "[C]\\d+", true); // Upper 'C' followed by numbers only
        addValidationListener(txtName, "[a-zA-Z]+", true); // Letters only
        addValidationListener(txtNic, "[0-9V]+", true); // Numbers and upper 'V' only
        addValidationListener(txtAddress, "[a-zA-Z\\s]+", true); // Letters and spaces only
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


//    ------------------------ End of Validation Part  ---------------------------------





    private void showSelectedUserDetails() {
        CustomerTm selectedUser = tblCustomer.getSelectionModel().getSelectedItem();
        tblCustomer.setOnMouseClicked(event -> showSelectedUserDetails());
        if (selectedUser != null) {
            txtCustomerId.setText(selectedUser.getCustomerID());
            txtName.setText(selectedUser.getCustomerName());
            txtNic.setText(selectedUser.getNIC());
            txtAddress.setText(selectedUser.getCustomerAddress());
            txtTel.setText(selectedUser.getCustomerTel());
            txtStatus.setText(selectedUser.getStatus());
        }
    }

    private void getCurrentCustomerId() {
        try {
            String currentId = CustomerRepo.getCurrentId();

            String nextCustomerId = generateNextCustomerId(currentId);
            txtCustomerId.setText(nextCustomerId);
            txtStatus.setText("Active");

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
        String id = txtCustomerId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();
        String status = txtStatus.getText();
        String userId = "U01";

        Customer customer = new Customer(id, nic, name, address, tel, userId, status);

        try {
            boolean isUpdated = CustomerRepo.update(customer);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtCustomerId.getText();
        String nic = txtNic.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();
        String userId = "U01";
        String status = "Active";

        Customer customer = new Customer(id, nic, name, address, tel, userId, status);

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
    }


    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String nic = txtNic.getText();

        Customer1 customer1 = CustomerRepo.nicSearch(nic);
        if (customer1 != null) {
            txtCustomerId.setText(customer1.getCustomerId());
            txtName.setText(customer1.getCustomerName());
            txtAddress.setText(customer1.getCustomerAddress());
            txtTel.setText(customer1.getCustomerTel());
            txtStatus.setText(customer1.getStatus());
        }else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
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
        txtCustomerId.setText("");
        txtNic.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtTel.setText("");
    }

    @FXML
    public void btnActiveOnAction(ActionEvent actionEvent) {
        txtStatus.setText("Active");
    }
}