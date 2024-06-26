package lk.ijse.tailorshopmanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lk.ijse.tailorshopmanagementsystem.Util.Regex;
import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.repository.EmployeeRepo;
import lk.ijse.tailorshopmanagementsystem.repository.UserRepo;
import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Data
public class RegistrationFormController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtId;


    public void initialize() {
        getCurrentUserId();
    }


    private void getCurrentUserId() {
        try {
            String currentId = UserRepo.getUserId();

            String nextUserId = generateNextUserId(currentId);
            txtId.setText(nextUserId);

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
        return "O1";
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        if (isValied()) {

            String id = txtId.getText();
            String name = txtName.getText();
            String password = txtPassword.getText();
            String email = txtEmail.getText();

            try {
                boolean isSaved = UserRepo.saveUser(id, name, email, password);
                if(isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "user saved!").show();
                    clearField();
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

   void clearField(){
        txtPassword.setText("");
        txtEmail.setText("");
        txtId.setText("");
        txtName.setText("");
        initialize();
   }

    @FXML
    void lblPasswordOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String password = txtPassword.getText();
        String email = txtEmail.getText();

        try {
            boolean isSaved = UserRepo.saveUser(id, name, email, password);
            if(isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "user saved!").show();
                clearField();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    public boolean isValied(){
        boolean name = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
        boolean email = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.EMAIL, txtEmail);
        boolean id = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.UID, txtId);

        return name && email && id ;
    }



    public void userIdOnKeyReleased(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.UID, txtId);
    }

    public void emailOnKeyReleased(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.EMAIL, txtEmail);
    }

    public void userNameOnKeyReleased(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
    }










}