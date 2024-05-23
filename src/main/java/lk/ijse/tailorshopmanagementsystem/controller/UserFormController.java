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
import lk.ijse.tailorshopmanagementsystem.Util.Regex;
import lk.ijse.tailorshopmanagementsystem.model.Supplier;
import lk.ijse.tailorshopmanagementsystem.model.User;
import lk.ijse.tailorshopmanagementsystem.model.tm.UserTm;
import lk.ijse.tailorshopmanagementsystem.repository.SupplierRepo;
import lk.ijse.tailorshopmanagementsystem.repository.UserRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserFormController {

    public AnchorPane rootNode;

    @FXML
    private TextField txtPassword;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colPassword;
    @FXML
    private TableColumn<?, ?> colStatus;
    @FXML
    private TableColumn<?, ?> colUserName;
    @FXML
    private TableView<UserTm> tblUser;

    @FXML
    private Label lblUserId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtStatus;

    public void initialize(){
        txtStatus.setText("Active");
        showSelectedUserDetails();
        getCurrentUserId();
        setCellValueFactory();
        loadAllUsers();
    }

    private void showSelectedUserDetails() {
        UserTm selectedUser = tblUser.getSelectionModel().getSelectedItem();
        tblUser.setOnMouseClicked(event -> showSelectedUserDetails());
        if (selectedUser != null) {
            lblUserId.setText(selectedUser.getUserID());
            txtName.setText(selectedUser.getUserName());
            txtEmail.setText(selectedUser.getUserEmail());
            txtPassword.setText(selectedUser.getPassword());
            txtStatus.setText(selectedUser.getStatus());

        }
    }

    private void getCurrentUserId() {
        try {
            String currentId = UserRepo.getUserId();

            String nextUserId = generateNextUserId(currentId);
            lblUserId.setText(nextUserId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextUserId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("U");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "U0" + ++idNum;
        }
        return "UO1";
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAllUsers() {
        ObservableList<UserTm> obList = FXCollections.observableArrayList();

        try {
            List<User> userList = UserRepo.getAll();
            for (User user : userList) {
                UserTm tm = new UserTm(
                        user.getUserID(),
                        user.getUserName(),
                        user.getUserEmail(),
                        user.getPassword(),
                        user.getStatus()
                );

                obList.add(tm);
            }

            tblUser.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnActiveOnAction(ActionEvent event) {
        txtStatus.setText("Active");
    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        lblUserId.setText("");
        txtEmail.setText("");
        txtName.setText("");
        txtStatus.setText("Active");
        txtPassword.setText("");

        loadAllUsers();
        getCurrentUserId();
    }
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = lblUserId.getText();

        try {
            boolean isDeleted = UserRepo.delete(id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "user deleted!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void emailKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.EMAIL, txtEmail);
    }

    public void passwordKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.ANY, txtPassword);
    }

    public void statusKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.STATUS, txtStatus);
    }

    public void nameKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
    }

    public boolean isValied(){
        boolean name = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
        boolean email = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.EMAIL, txtEmail);
        boolean password = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.ANY, txtPassword);
        boolean status = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.STATUS, txtStatus);


        return name && email && password && status ;
    }
    @FXML
    void btnSaveOnAction(ActionEvent event) {

        if (isValied()) {
            String id = lblUserId.getText();
            String userName = txtName.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            String status = txtStatus.getText();

            User user = new User(id, userName, email, password, status);

            try {
                boolean isSaved = UserRepo.save(user);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "user saved!").show();
                    clearFields();
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
    void btnUpdateOnAction(ActionEvent event) {
        if (isValied()) {
            String id = lblUserId.getText();
            String name = txtName.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            String status = txtStatus.getText();

            User user = new User(id, name, email, password, status);

            try {
                boolean isUpdated = UserRepo.update(user);
                if(isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "user updated!").show();
                    clearFields();
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
