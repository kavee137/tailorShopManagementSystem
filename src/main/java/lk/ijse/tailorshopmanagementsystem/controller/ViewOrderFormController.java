//package lk.ijse.tailorshopmanagementsystem.controller;
//
//public class ViewOrderFormController {

//
//    SELECT
//    c.NIC,
//    c.customerName,
//    c.customerAddress,
//    c.customerTel,
//    o.orderID,
//    o.employeeID,  -- Added employeeID
//    o.orderDate,
//    o.returnDate,
//    o.status AS orderStatus,
//    e.employeeName,
//    od.fabricID,
//    od.description,
//    od.measurements,
//    od.fabricSize,
//    od.unitPrice,
//    od.qty,
//    od.total,
//    f.fabricName,
//    f.fabricColor,
//    p.paymentID,
//    p.paymentType,
//    p.price AS paymentPrice
//            FROM
//    customer c
//    JOIN
//    orders o ON c.customerID = o.customerID
//            JOIN
//    employee e ON o.employeeID = e.employeeID
//            JOIN
//    orderDetails od ON o.orderID = od.orderID
//            JOIN
//    fabric f ON od.fabricID = f.fabricID
//            JOIN
//    payment p ON o.paymentID = p.paymentID;

//    SELECT c.NIC, c.customerName, c.customerAddress, c.customerTel, o.orderID, o.employeeID, o.orderDate, o.returnDate, o.status AS orderStatus, e.employeeName, od.fabricID, od.description, od.measurements, od.fabricSize, od.unitPrice, od.qty, od.total, f.fabricName, f.fabricColor, p.paymentID, p.paymentType, p.price AS paymentPrice FROM customer c JOIN orders o ON c.customerID = o.customerID JOIN employee e ON o.employeeID = e.employeeID JOIN orderDetails od ON o.orderID = od.orderID JOIN fabric f ON od.fabricID = f.fabricID JOIN payment p ON o.paymentID = p.paymentID;

//    SELECT o.orderID, p.paymentID, c.NIC, o.orderDate, o.returnDate, o.status AS orderStatus, c.customerName, c.customerAddress, c.customerTel , o.employeeID,  e.employeeName, od.fabricID, f.fabricName, f.fabricColor,od.description, od.measurements, od.fabricSize, od.unitPrice, od.qty, od.total, p.paymentType, p.price AS paymentPrice FROM customer c JOIN orders o ON c.customerID = o.customerID JOIN employee e ON o.employeeID = e.employeeID JOIN orderDetails od ON o.orderID = od.orderID JOIN fabric f ON od.fabricID = f.fabricID JOIN payment p ON o.paymentID = p.paymentID;

//}


package lk.ijse.tailorshopmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.tailorshopmanagementsystem.model.tm.OrderViewTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.ReservationViewTm;
import lk.ijse.tailorshopmanagementsystem.repository.ViewOrderRepo;
import lk.ijse.tailorshopmanagementsystem.repository.ViewReservationRepo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewOrderFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colCustomerName;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colEmployeeName;

    @FXML
    private TableColumn<?, ?> colFabricColor;

    @FXML
    private TableColumn<?, ?> colFabricId;

    @FXML
    private TableColumn<?, ?> colFabricName;

    @FXML
    private TableColumn<?, ?> colFabricSize;

    @FXML
    private TableColumn<?, ?> colMeasurements;

    @FXML
    private TableColumn<?, ?> colNic;

    @FXML
    private TableColumn<?, ?> colOrderDate;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colPaymentId;

    @FXML
    private TableColumn<?, ?> colPaymentType;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colReturnDate;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private DatePicker dpDateFrom;

    @FXML
    private DatePicker dpDateTo;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<OrderViewTm> tblViewOrders;

    private ObservableList<OrderViewTm> obList = FXCollections.observableArrayList();


    public void initialize() throws SQLException {
        setCellValueFactory();
        loadAllProcessingOrders();
    }

    private void setCellValueFactory(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("customerTel"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colFabricId.setCellValueFactory(new PropertyValueFactory<>("fabricID"));
        colFabricName.setCellValueFactory(new PropertyValueFactory<>("fabricName"));
        colFabricColor.setCellValueFactory(new PropertyValueFactory<>("fabricColor"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colMeasurements.setCellValueFactory(new PropertyValueFactory<>("measurements"));
        colFabricSize.setCellValueFactory(new PropertyValueFactory<>("fabricSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPaymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("paymentPrice"));
    }

    public void loadAllProcessingOrders() throws SQLException {
        tblViewOrders.getItems().clear();

        ResultSet reservationDetails = ViewOrderRepo.getProcessingOrderDetails();

        while (reservationDetails.next()) {
            OrderViewTm r = new OrderViewTm();
            r.setOrderID(reservationDetails.getString("orderID"));
            r.setPaymentID(reservationDetails.getString("paymentID"));
            r.setNIC(reservationDetails.getString("NIC"));
            r.setOrderDate(reservationDetails.getDate("orderDate"));
            r.setReturnDate(reservationDetails.getDate("returnDate"));
            r.setOrderStatus(reservationDetails.getString("orderStatus"));
            r.setCustomerName(reservationDetails.getString("customerName"));
            r.setCustomerAddress(reservationDetails.getString("customerAddress"));
            r.setCustomerTel(reservationDetails.getString("customerTel"));
            r.setEmployeeID(reservationDetails.getString("employeeID"));
            r.setEmployeeName(reservationDetails.getString("employeeName"));
            r.setFabricID(reservationDetails.getString("fabricID"));
            r.setFabricName(reservationDetails.getString("fabricName"));
            r.setFabricColor(reservationDetails.getString("fabricColor"));
            r.setDescription(reservationDetails.getString("description"));
            r.setMeasurements(reservationDetails.getString("measurements"));
            r.setFabricSize(reservationDetails.getDouble("fabricSize"));
            r.setUnitPrice(reservationDetails.getDouble("unitPrice"));
            r.setQty(reservationDetails.getInt("qty"));
            r.setTotal(reservationDetails.getDouble("total"));
            r.setPaymentType(reservationDetails.getString("paymentType"));
            r.setPaymentPrice(reservationDetails.getDouble("paymentPrice"));

            obList.add(r);
        }

        tblViewOrders.setItems(obList);
    }

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException {
        dpDateTo.setValue(null);
        dpDateFrom.setValue(null);

        initialize();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException {
        if (dpDateFrom.getValue() != null && dpDateTo.getValue() != null) {

            Date dateFrom = Date.valueOf(dpDateFrom.getValue());
            Date dateTo = Date.valueOf(dpDateTo.getValue());

            tblViewOrders.getItems().clear();

            ResultSet orderDetails = ViewOrderRepo.getOrderDetails(dateFrom, dateTo);

            while (orderDetails.next()) {
                OrderViewTm r = new OrderViewTm();
                r.setOrderID(orderDetails.getString("orderID"));
                r.setPaymentID(orderDetails.getString("paymentID"));
                r.setNIC(orderDetails.getString("NIC"));
                r.setOrderDate(orderDetails.getDate("orderDate"));
                r.setReturnDate(orderDetails.getDate("returnDate"));
                r.setOrderStatus(orderDetails.getString("orderStatus"));
                r.setCustomerName(orderDetails.getString("customerName"));
                r.setCustomerAddress(orderDetails.getString("customerAddress"));
                r.setCustomerTel(orderDetails.getString("customerTel"));
                r.setEmployeeID(orderDetails.getString("employeeID"));
                r.setEmployeeName(orderDetails.getString("employeeName"));
                r.setFabricID(orderDetails.getString("fabricID"));
                r.setFabricName(orderDetails.getString("fabricName"));
                r.setFabricColor(orderDetails.getString("fabricColor"));
                r.setDescription(orderDetails.getString("description"));
                r.setMeasurements(orderDetails.getString("measurements"));
                r.setFabricSize(orderDetails.getDouble("fabricSize"));
                r.setUnitPrice(orderDetails.getDouble("unitPrice"));
                r.setQty(orderDetails.getInt("qty"));
                r.setTotal(orderDetails.getDouble("total"));
                r.setPaymentType(orderDetails.getString("paymentType"));
                r.setPaymentPrice(orderDetails.getDouble("paymentPrice"));

                obList.add(r);
            }


        } else {
            // Show error message if validation fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Order Search error");
            alert.setHeaderText("Order search Failed");
            alert.setContentText("Please select return date period!");
            alert.showAndWait();
        }


    }

    @FXML
    void btnShowAllOnAction(ActionEvent event) throws SQLException {
        tblViewOrders.getItems().clear();

        ResultSet orderDetails = ViewOrderRepo.getAll();

        while (orderDetails.next()) {
            OrderViewTm r = new OrderViewTm();
            r.setOrderID(orderDetails.getString("orderID"));
            r.setPaymentID(orderDetails.getString("paymentID"));
            r.setNIC(orderDetails.getString("NIC"));
            r.setOrderDate(orderDetails.getDate("orderDate"));
            r.setReturnDate(orderDetails.getDate("returnDate"));
            r.setOrderStatus(orderDetails.getString("orderStatus"));
            r.setCustomerName(orderDetails.getString("customerName"));
            r.setCustomerAddress(orderDetails.getString("customerAddress"));
            r.setCustomerTel(orderDetails.getString("customerTel"));
            r.setEmployeeID(orderDetails.getString("employeeID"));
            r.setEmployeeName(orderDetails.getString("employeeName"));
            r.setFabricID(orderDetails.getString("fabricID"));
            r.setFabricName(orderDetails.getString("fabricName"));
            r.setFabricColor(orderDetails.getString("fabricColor"));
            r.setDescription(orderDetails.getString("description"));
            r.setMeasurements(orderDetails.getString("measurements"));
            r.setFabricSize(orderDetails.getDouble("fabricSize"));
            r.setUnitPrice(orderDetails.getDouble("unitPrice"));
            r.setQty(orderDetails.getInt("qty"));
            r.setTotal(orderDetails.getDouble("total"));
            r.setPaymentType(orderDetails.getString("paymentType"));
            r.setPaymentPrice(orderDetails.getDouble("paymentPrice"));

            obList.add(r);
        }

    }

    @FXML
    public void btnShowCompletedOnAction(ActionEvent actionEvent) throws SQLException {
        tblViewOrders.getItems().clear();

        ResultSet orderDetails = ViewOrderRepo.getCompleted();
        while (orderDetails.next()) {
            OrderViewTm r = new OrderViewTm();
            r.setOrderID(orderDetails.getString("orderID"));
            r.setPaymentID(orderDetails.getString("paymentID"));
            r.setNIC(orderDetails.getString("NIC"));
            r.setOrderDate(orderDetails.getDate("orderDate"));
            r.setReturnDate(orderDetails.getDate("returnDate"));
            r.setOrderStatus(orderDetails.getString("orderStatus"));
            r.setCustomerName(orderDetails.getString("customerName"));
            r.setCustomerAddress(orderDetails.getString("customerAddress"));
            r.setCustomerTel(orderDetails.getString("customerTel"));
            r.setEmployeeID(orderDetails.getString("employeeID"));
            r.setEmployeeName(orderDetails.getString("employeeName"));
            r.setFabricID(orderDetails.getString("fabricID"));
            r.setFabricName(orderDetails.getString("fabricName"));
            r.setFabricColor(orderDetails.getString("fabricColor"));
            r.setDescription(orderDetails.getString("description"));
            r.setMeasurements(orderDetails.getString("measurements"));
            r.setFabricSize(orderDetails.getDouble("fabricSize"));
            r.setUnitPrice(orderDetails.getDouble("unitPrice"));
            r.setQty(orderDetails.getInt("qty"));
            r.setTotal(orderDetails.getDouble("total"));
            r.setPaymentType(orderDetails.getString("paymentType"));
            r.setPaymentPrice(orderDetails.getDouble("paymentPrice"));

            obList.add(r);
        }
    }

}
