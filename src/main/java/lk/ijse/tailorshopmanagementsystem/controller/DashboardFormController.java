package lk.ijse.tailorshopmanagementsystem.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardFormController {

    public JFXButton dButton;
    @FXML
    private AnchorPane rootNode;

    @FXML
    private VBox vboxButtons;

    // Initialize method to load the main dashboard after a delay
    public void initialize() {
        // Load mainDashboard_form.fxml after a 2-second delay
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> loadMainDashboard());
        delay.play();

        dButton.setStyle("-fx-background-color: #192a51");

        // Set action listeners for each button in the VBox
        setButtonActions();
    }

    // Method to load the main dashboard
    private void loadMainDashboard() {
        try {
            // Load mainDashboard_form.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainDashboard_form.fxml"));
            AnchorPane mainDashboard = loader.load();

            // Replace the children of rootNode with the loaded content
            rootNode.getChildren().setAll(mainDashboard);
            Stage stage = (Stage) rootNode.getScene().getWindow();
            stage.setTitle("Dashboard");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle logout button action
    @FXML
    private void btnLogoutOnAction(ActionEvent actionEvent) throws IOException {
        // Load the login form
        Parent loginForm = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(loginForm);
        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
    }

    // Method to handle button clicks
    private void setButtonActions() {
        for (javafx.scene.Node node : vboxButtons.getChildren()) {
            if (node instanceof JFXButton) {
                JFXButton button = (JFXButton) node;
                button.setOnAction(event -> handleButtonAction(button));
            }
        }
    }

    // Method to handle button action
    private void handleButtonAction(JFXButton clickedButton) {
        // Reset all button colors
        for (javafx.scene.Node node : vboxButtons.getChildren()) {
            if (node instanceof JFXButton) {
                JFXButton button = (JFXButton) node;
                button.setStyle("-fx-background-color: #1b91ff");
            }
        }

        // Change the color of the clicked button
        clickedButton.setStyle("-fx-background-color: #192a51");

        // Perform other actions based on the clicked button
        String buttonText = clickedButton.getText();
        switch (buttonText) {
            case "Dashboard":
                // Load the dashboard view
                loadMainDashboard();
                break;
            case "Customer":
                try {
                    // Load the customer form
                    FXMLLoader customerLoader = new FXMLLoader(getClass().getResource("/view/customer_form.fxml"));
                    Parent customerRoot = customerLoader.load();
                    rootNode.getChildren().clear();
                    rootNode.getChildren().add(customerRoot);

                    Stage stage = (Stage) rootNode.getScene().getWindow();
                    stage.setTitle("Customer Manage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Product":
                try {
                    // Load the product form
                    FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/view/product_form.fxml"));
                    Parent productRoot = productLoader.load();
                    rootNode.getChildren().clear();
                    rootNode.getChildren().add(productRoot);

                    Stage stage = (Stage) rootNode.getScene().getWindow();
                    stage.setTitle("Product Manage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Orders":
                try {
                    // Load the orders form
                    FXMLLoader ordersLoader = new FXMLLoader(getClass().getResource("/view/orders_form.fxml"));
                    Parent ordersRoot = ordersLoader.load();
                    rootNode.getChildren().clear();
                    rootNode.getChildren().add(ordersRoot);

                    Stage stage = (Stage) rootNode.getScene().getWindow();
                    stage.setTitle("Order Manage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Employee":
                try {
                    // Load the employee form
                    FXMLLoader employeeLoader = new FXMLLoader(getClass().getResource("/view/employee_form.fxml"));
                    Parent employeeRoot = employeeLoader.load();
                    rootNode.getChildren().clear();
                    rootNode.getChildren().add(employeeRoot);

                    Stage stage = (Stage) rootNode.getScene().getWindow();
                    stage.setTitle("Employee Manage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Reservation":
                try {
                    // Load the reservation form
                    FXMLLoader reservationLoader = new FXMLLoader(getClass().getResource("/view/reservation_form.fxml"));
                    Parent reservationRoot = reservationLoader.load();
                    rootNode.getChildren().clear();
                    rootNode.getChildren().add(reservationRoot);

                    Stage stage = (Stage) rootNode.getScene().getWindow();
                    stage.setTitle("Reservation Manage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Supplier":
                try {
                    // Load the supplier form
                    FXMLLoader supplierLoader = new FXMLLoader(getClass().getResource("/view/supplier_form.fxml"));
                    Parent supplierRoot = supplierLoader.load();
                    rootNode.getChildren().clear();
                    rootNode.getChildren().add(supplierRoot);

                    Stage stage = (Stage) rootNode.getScene().getWindow();
                    stage.setTitle("Supplier Manage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Fabric":
                try {
                    // Load the supplier form
                    FXMLLoader supplierLoader = new FXMLLoader(getClass().getResource("/view/fabric_form.fxml"));
                    Parent supplierRoot = supplierLoader.load();
                    rootNode.getChildren().clear();
                    rootNode.getChildren().add(supplierRoot);

                    Stage stage = (Stage) rootNode.getScene().getWindow();
                    stage.setTitle("Fabric Manage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "User":
                try {
                    // Load the user form
                    FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/view/user_form.fxml"));
                    Parent userRoot = userLoader.load();
                    rootNode.getChildren().clear();
                    rootNode.getChildren().add(userRoot);

                    Stage stage = (Stage) rootNode.getScene().getWindow();
                    stage.setTitle("User Manage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }


    public void btnDashboardOnAction(ActionEvent actionEvent) {

    }

    public void btnReservationOnAction(ActionEvent actionEvent) {

    }

    public void btnOrdersOnAction(ActionEvent actionEvent) {

    }

    public void btnCustomerOnAction(ActionEvent actionEvent) {

    }

    public void btnProductOnAction(ActionEvent actionEvent) {

    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) {

    }

    public void btnSupplierOnAction(ActionEvent actionEvent) {

    }

    public void btnUserOnAction(ActionEvent actionEvent) {

    }

    public void btnFabricOnAction(ActionEvent actionEvent) {

    }
}
