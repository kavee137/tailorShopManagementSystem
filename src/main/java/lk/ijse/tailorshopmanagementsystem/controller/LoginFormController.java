package lk.ijse.tailorshopmanagementsystem.controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.tailorshopmanagementsystem.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public TextField txtUserId;
    public TextField txtPassword;

    public AnchorPane rootNode;


    public void initialize() {
        initializeValidation();
    }


    private void initializeValidation() {
        addValidationListener(txtUserId, "[a-zA-Z]+", true); // Letters only

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














    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        String userId = txtUserId.getText();
        String pw = txtPassword.getText();

        try {
            checkCredential(userId, pw);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void checkCredential(String userId, String pw) throws SQLException, IOException {
        String sql = "SELECT userName, password FROM user WHERE userName = ? AND status = 'Active'";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, userId);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String dbPw = resultSet.getString("password");

            if (pw.equals(dbPw)) {
//                navigateToTheDashboard();
                navigateToTheDashboard((Stage) rootNode.getScene().getWindow());

            } else {
                new Alert(Alert.AlertType.ERROR, "sorry! password is incorrect!").show();
            }

        } else {
            new Alert(Alert.AlertType.INFORMATION, "sorry! user id can't be find!").show();
        }
    }

    private void navigateToTheDashboard(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard_form.fxml"));
        AnchorPane rootNode = loader.load();
        DashboardFormController controller = loader.getController();
        controller.initialize(); // Assuming you want to initialize the dashboard controller

        Scene scene = new Scene(rootNode);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard");
    }

    public void linkRegistrationOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/registration_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.setTitle("Registration Form");

        stage.show();
    }


    public void txtUserNameOnKeyReleased(KeyEvent keyEvent) {
    }


}