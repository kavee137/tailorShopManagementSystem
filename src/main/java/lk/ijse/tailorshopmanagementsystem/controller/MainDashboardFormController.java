package lk.ijse.tailorshopmanagementsystem.controller;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import lk.ijse.tailorshopmanagementsystem.model.*;
import lk.ijse.tailorshopmanagementsystem.repository.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.sql.SQLException;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lk.ijse.tailorshopmanagementsystem.model.Fabric;
import lk.ijse.tailorshopmanagementsystem.repository.MainDashBoardRepo;

import java.sql.SQLException;
import java.util.List;

import javafx.animation.ScaleTransition;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;


public class MainDashboardFormController {


    public Label lblEmployeeCount;
    public Label lblOrderCount;
    public Label lblReservationCount;
    public Label lblTransactionCount;
    public Label lblProductCount;
    public Label lblSupplierCount;
    public Label lblUserCount;

    public Label date;
    public Label time;

    @FXML
    private Label lblBlazerColor;

    @FXML
    private Label lblBlazerId;

    @FXML
    private Label lblBlazerQtyOnHand;

    @FXML
    private Label lblBlazerSize;

    @FXML
    private Label lblTrouserColor;

    @FXML
    private Label lblTrouserId;

    @FXML
    private Label lblTrouserQtyOnHand;

    @FXML
    private Label lblTrouserSize;

    @FXML
    private Label lblUserName;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;  // This is your FXML PieChart node

    @FXML
    private Label lblCustomerCount;

    @FXML
    private AnchorPane rootNode;

    public void initialize() throws SQLException {

        loadActiveCustomerCount();
        loadActiveEmployeeCount();
        loadProcessingOrderCount();
        loadProcessingReservationCount();
        loadTransaction();
        loadProduct();
        loadSupplier();
        loadUser();
        setLowFabricPieChart();
        initializeDateTime();
        setBarChart();
        setMostReservedProducts();
    }

    public void setMostReservedProducts() throws SQLException {
        List<Product> pDetails = MainDashBoardRepo.getMostReservedProduct();

        // Assuming the most reserved product is at index 0
        if (!pDetails.isEmpty()) {
            lblBlazerId.setText(pDetails.get(0).getProductID());
            lblBlazerColor.setText(pDetails.get(0).getProductColor());
            lblBlazerSize.setText(pDetails.get(0).getProductSize());
            lblBlazerQtyOnHand.setText(String.valueOf(pDetails.get(0).getQtyOnHand()));


            lblTrouserId.setText(pDetails.get(1).getProductID());
            lblTrouserColor.setText(pDetails.get(1).getProductColor());
            lblTrouserSize.setText(pDetails.get(1).getProductSize());
            lblTrouserQtyOnHand.setText(String.valueOf(pDetails.get(1).getQtyOnHand()));


        } else {
            // Handle case when the list is empty
            new Alert(Alert.AlertType.ERROR, "No data to show most reservation products!").show();
        }
    }

//    public void setBarChart() {
//        try {
//            List<MonthlySales> monthlySales = getMonthlySales();
//
//            // Clear existing data from the chart
//            barChart.getData().clear();
//
//            // Define axes
//            CategoryAxis xAxis = new CategoryAxis();
//            xAxis.setLabel("Month");
//            NumberAxis yAxis = new NumberAxis();
//            yAxis.setLabel("Total Sales");
//
//            // Create a series for the data
//            XYChart.Series<String, Number> series = new XYChart.Series<>();
//            series.setName("Monthly Sales");
//
//            // Add data to the series
//            for (MonthlySales sale : monthlySales) {
//                series.getData().add(new XYChart.Data<>(Integer.toString(sale.getOrderMonth()), sale.getTotalSales()));
//            }
//
//            // Add the series to the chart
//            barChart.getData().add(series);
//        } catch (SQLException e) {
//            new Alert(Alert.AlertType.ERROR, "Error retrieving monthly sales data").show();
//        }
//    }
//
//    private List<MonthlySales> getMonthlySales() throws SQLException {
//        return MainDashBoardRepo.getMonthlySalesFor2024();
//    }

    public void setBarChart() {
        try {
            List<MonthlySales> monthlySales = getMonthlySales();

            // Clear existing data from the chart
            barChart.getData().clear();

            // Define axes
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Month");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Total Sales");

            // Create a series for the data
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Monthly Sales");

            // Add data to the series with animations
            for (MonthlySales sale : monthlySales) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(Integer.toString(sale.getOrderMonth()), sale.getTotalSales());
                series.getData().add(data);

                // Add animations to each bar
                data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        // Translate Transition
                        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), newNode);
                        translateTransition.setFromY(-newNode.getBoundsInParent().getHeight());
                        translateTransition.setToY(0);

                        // Fade Transition
                        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), newNode);
                        fadeTransition.setFromValue(0);
                        fadeTransition.setToValue(1);

                        // Scale Transition
                        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), newNode);
                        scaleTransition.setFromX(0);
                        scaleTransition.setFromY(0);
                        scaleTransition.setToX(1);
                        scaleTransition.setToY(1);

                        // Rotate Transition
                        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000), newNode);
                        rotateTransition.setByAngle(360);

                        // Parallel Transition to combine all transitions
                        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, fadeTransition, scaleTransition, rotateTransition);

                        parallelTransition.play();
                    }
                });
            }

            // Add the series to the chart
            barChart.getData().add(series);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error retrieving monthly sales data").show();
        }
    }

    private List<MonthlySales> getMonthlySales() throws SQLException {
        return MainDashBoardRepo.getMonthlySalesFor2024();
    }

    private void initializeDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    LocalDateTime now = LocalDateTime.now();
                    date.setText(now.format(dateFormatter));
                    time.setText(now.format(timeFormatter));
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setLowFabricPieChart() {
        try {
            List<Fabric> fabrics = MainDashBoardRepo.getLowStockFabrics();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (Fabric fabric : fabrics) {
                // Use the fabric name and color as the label and the quantity on hand as the value
                String label = fabric.getFabricName() + " (" + fabric.getFabricColor() + ") " + fabric.getFabricQtyOnHand();
                int value = fabric.getFabricQtyOnHand();
                PieChart.Data data = new PieChart.Data(label, value);
                pieChartData.add(data);

                // Add animations to each data slice
                data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        // Scale Transition
                        newNode.setScaleX(0);
                        newNode.setScaleY(0);
                        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), newNode);
                        scaleTransition.setToX(1);
                        scaleTransition.setToY(1);

                        // Fade Transition
                        newNode.setOpacity(0);
                        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), newNode);
                        fadeTransition.setToValue(1);

                        // Rotate Transition
                        newNode.setRotate(0);
                        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000), newNode);
                        rotateTransition.setByAngle(360);

                        // Translate Transition
                        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), newNode);
                        translateTransition.setFromY(-20);
                        translateTransition.setToY(0);

                        // Fill Transition (color change effect)
                        FillTransition fillTransition = new FillTransition(Duration.millis(1000));
                        fillTransition.setFromValue(Color.RED);
                        fillTransition.setToValue(Color.BLUE);

                        // Play animations sequentially
                        scaleTransition.play();
                        fadeTransition.play();
                        rotateTransition.play();
                        translateTransition.play();
                        fillTransition.play();
                    }
                });
            }

            pieChart.setData(pieChartData);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


//    private void setLowFabricPieChart() {
//        try {
//            List<Fabric> fabrics = MainDashBoardRepo.getLowStockFabrics();
//
//            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
//
//            for (Fabric fabric : fabrics) {
//                // Use the fabric name and color as the label and the quantity on hand as the value
//                String label = fabric.getFabricName() + " (" + fabric.getFabricColor() + ")  " + fabric.getFabricQtyOnHand();
//                int value = fabric.getFabricQtyOnHand();
//                pieChartData.add(new PieChart.Data(label, value));
//            }
//
//            pieChart.setData(pieChartData);
//        } catch (SQLException e) {
//            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
//        }
//    }


    private void loadUser() throws SQLException {
        String count = UserRepo.getTotalUser();
        lblUserCount.setText(count);
    }

    private void loadSupplier() throws SQLException {
        String count = SupplierRepo.getTotalSuppliers();
        lblSupplierCount.setText(count);
    }

    private void loadProduct() throws SQLException {
        String count = ProductRepo.getTotalProduct();
        lblProductCount.setText(count);
    }

    private void loadTransaction() throws SQLException {
        String count = PaymentRepo.getTotalPayment();
        lblTransactionCount.setText(count);
    }

    private void loadProcessingReservationCount() throws SQLException {
        String count = ReservationRepo.getReservationCount();
        lblReservationCount.setText(count);
    }

    private void loadProcessingOrderCount() throws SQLException {
        String count = OrderRepo.getOrderCount();
        lblOrderCount.setText(count);
    }

    private void loadActiveEmployeeCount() throws SQLException {
        String count = EmployeeRepo.getEmployeeCount();
        lblEmployeeCount.setText(count);
    }

    private void loadActiveCustomerCount() throws SQLException {
        String count = CustomerRepo.getCustomerCount();
        lblCustomerCount.setText(count);
    }

}
