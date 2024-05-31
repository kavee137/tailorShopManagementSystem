package lk.ijse.tailorshopmanagementsystem.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.tailorshopmanagementsystem.Util.Regex;
import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.Order;
import lk.ijse.tailorshopmanagementsystem.model.OrderDetails;
import lk.ijse.tailorshopmanagementsystem.model.PlaceOrder;
import lk.ijse.tailorshopmanagementsystem.model.tm.CartTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.ReservationTm;
import lk.ijse.tailorshopmanagementsystem.repository.OrderRepo;
import lk.ijse.tailorshopmanagementsystem.repository.PlaceOrderRepo;
import lk.ijse.tailorshopmanagementsystem.repository.ReservationRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class PlaceOrderFormController  {
    @FXML
    private JFXComboBox<String> cmbStatus;

    @FXML
    private Label lblCustomerId;

    @FXML
    private Label lblNetTotal;

    @FXML
    private TableColumn<?, ?> colFabric;

    @FXML
    private TableColumn<?, ?> colColor;
    @FXML
    public TableColumn<?, ?> colFabricSize;

    @FXML
    private TableColumn<?, ?> colMeasurements;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private Label lblPaymentId;

    @FXML
    private JFXComboBox<String> cmbPaymentType;

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblCustomerName;

    @FXML
    private JFXComboBox<String> cmbTailorId;

    @FXML
    private JFXComboBox<String> cmbFabricName;

    @FXML
    private JFXComboBox<String> cmbFabricColor;

    @FXML
    private TableView<CartTm> tblOrderCart;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtFabricSize;

    @FXML
    private DatePicker returnDate;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtOrderId;

    @FXML
    private Label lblOrderId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtMeasurements;

    @FXML
    private Label lblOrderDate;

    public AnchorPane pane;


    public AnchorPane rootNode;

    private ObservableList<CartTm> obList = FXCollections.observableArrayList();



    public void initialize() {
        setDate();
        getCurrentOrderId();
        setCmbStatus();
        getFabricName();
        getTailorId();
        setPaymentType();
        getCurrentPaymentId();
        setCellValueFactory();
    }

    void setCmbStatus() {
        ObservableList<String> status = FXCollections.observableArrayList();
        cmbStatus.setValue("processing");
        status.add("processing");
        status.add("completed");

        cmbStatus.setItems(status);
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws SQLException {

        if (!lblOrderId.getText().isEmpty() &&
                returnDate.getValue() != null &&
                !txtNic.getText().isEmpty() &&
                !lblEmployeeName.getText().isEmpty() &&
                cmbTailorId.getValue() != null &&
                !lblCustomerName.getText().isEmpty() &&
                !tblOrderCart.getItems().isEmpty()) {

            String orderId = lblOrderId.getText();
            String cusId = lblCustomerId.getText();
            int paymentId = Integer.parseInt(lblPaymentId.getText());
            String employeeID = (String) cmbTailorId.getValue();
            Date orderDate = Date.valueOf(lblOrderDate.getText());
            Date returnDateValue = Date.valueOf(returnDate.getValue());
            String paymentType = cmbPaymentType.getValue();
            double netTotal = Double.parseDouble(lblNetTotal.getText());
            String status = "processing";

            var order = new Order(orderId, cusId, paymentId, employeeID, orderDate, returnDateValue, status);

            List<OrderDetails> odList = new ArrayList<>();

            for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
                CartTm tm = obList.get(i);

                List<String> fabId = OrderRepo.getFabricId(obList.get(i).getFabricName(), obList.get(i).getFabricColor());
                System.out.println(fabId);

                OrderDetails od = new OrderDetails(
                        orderId,
                        fabId.get(0),
                        tm.getDescription(),
                        tm.getMeasurements(),
                        tm.getFabricSize(),
                        tm.getUnitPrice(),
                        tm.getQty(),
                        tm.getTotal()

                );

                odList.add(od);
            }

            PlaceOrder po = new PlaceOrder(order, odList);
            try {
                boolean isPlaced = PlaceOrderRepo.placeOrder(po, paymentType, netTotal);
                if (isPlaced) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
                    printBill();
                    clearFields();

                } else {
                    new Alert(Alert.AlertType.WARNING, "Order Placed Unsuccessfully!").show();
                }
            } catch (SQLException | JRException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }

        } else {
            showErrorAlert("Details Failed", "Please fill in all fields correctly.");
        }
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String qty1 = lblQtyOnHand.getText();

        if (qty1 == null || qty1.isEmpty()) {
            showErrorAlert("Fabric Details Failed", "Please select fabric details!");

        }else if (isAddToCartValid()) {
                addToCart();
        } else {
            showErrorAlert("Validation Failed", "Please fill in all fields correctly.");
        }
    }

    private boolean isAddToCartValid() {
            boolean fabSize = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.QTY, txtFabricSize);
            boolean description = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.DESCRIPTION, txtDescription);
            boolean measurements = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.ANY, txtMeasurements);
            boolean price = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.PRICE, txtPrice);
            boolean qty = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.QTY, txtQty);

            return fabSize && description && measurements && price && qty;
    }

    private void addToCart() {
        String description = txtDescription.getText();
        String fabricName = (String) cmbFabricName.getValue();
        String fabricColor = (String) cmbFabricColor.getValue();
        double fabricSize = Double.parseDouble(txtFabricSize.getText());
        String measurements = txtMeasurements.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(txtPrice.getText());
        double total = qty * unitPrice;

        // Create a remove button for the cart item
        JFXButton btnRemove = new JFXButton("Remove");
        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int selectedIndex = tblOrderCart.getSelectionModel().getSelectedIndex();
                obList.remove(selectedIndex);

                tblOrderCart.refresh();
                calculateNetTotal();
            }
        });

        // Create a new cart item and add it to the list
        CartTm cartItem = new CartTm(description, fabricName, fabricColor, fabricSize, measurements, unitPrice, qty, total, btnRemove);
        obList.add(cartItem);

        // Refresh the cart table with the updated list
        tblOrderCart.setItems(obList);
        removeForNewItem();
        calculateNetTotal();
    }

    private void showErrorAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void fabricSizeKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.FABSIZE, txtFabricSize);
    }

    public void descriptionKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.DESCRIPTION, txtDescription);
    }

    public void priceKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.PRICE, txtPrice);
    }

    public void qty2KeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.QTY, txtQty);
    }

    public void meKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.ANY, txtMeasurements);
    }

    private void calculateNetTotal() {
        double netTotal = 0;
        for (CartTm item : obList) {
            netTotal += item.getTotal();
        }
        lblNetTotal.setText(String.valueOf(netTotal));
    }

    private void setCellValueFactory() {
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colFabric.setCellValueFactory(new PropertyValueFactory<>("fabricName"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("fabricColor"));
        colFabricSize.setCellValueFactory(new PropertyValueFactory<>("fabricSize"));
        colMeasurements.setCellValueFactory(new PropertyValueFactory<>("measurements"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
    }

    private void getCurrentPaymentId() {
        try {
            int currentId = OrderRepo.getCurrentPaymentId();

            int nextPaymentId = 0;

            if (currentId != 0) {
                nextPaymentId = currentId + 1;
            }
            lblPaymentId.setText(String.valueOf(nextPaymentId));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPaymentType() {
        ObservableList<String> paymentType = FXCollections.observableArrayList();
        cmbPaymentType.setValue("Cash");

        paymentType.add("Cash");
        paymentType.add("Card");

        cmbPaymentType.setItems(paymentType);
    }

    private void getTailorId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> idList = OrderRepo.getEmployeeIds();

            for (String code : idList) {
                obList.add(code);
            }
            cmbTailorId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getEmployeeName() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        String id = (String) cmbTailorId.getValue();


        try {
            String name = OrderRepo.getEmployeeName(id);
            lblEmployeeName.setText(name);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
//        initialize();
    }

    private void clearFields() {
        txtDescription.setText("");
        txtNic.setText("");
        txtQty.setText("");
        txtPrice.setText("");
        txtFabricSize.setText("");
        lblQtyOnHand.setText(null);
        cmbFabricColor.setValue(null);
        cmbFabricName.setValue(null);
        returnDate.setValue(null);
        cmbFabricName.setValue(null);
        txtMeasurements.setText("");
        tblOrderCart.getItems().clear();
        obList.removeAll();
        cmbTailorId.setValue(null);
        lblEmployeeName.setText(null);
        lblCustomerId.setText(null);
        lblCustomerName.setText("");
        lblNetTotal.setText(null);
        txtOrderId.setText(null);
        setPaymentType();
        initialize();
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        lblOrderDate.setText(String.valueOf(now));
    }

    private void getFabricName() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = OrderRepo.getFabricName();

            for (String code : codeList) {
                obList.add(code);
            }
            cmbFabricName.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getColorsForFabric() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        String fabricName = (String) cmbFabricName.getValue();

        try {
            List<String> codeList = OrderRepo.getColorsForFabric(fabricName);

            for (String code : codeList) {
                obList.add(code);
            }
            cmbFabricColor.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    String currentId1 = null;
    private void getCurrentOrderId() {
        try {
            String currentId = OrderRepo.getCurrentId();
            currentId1 = currentId;

            String nextOrderId = generateNextOrderId(currentId);
            lblOrderId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextOrderId(String currentId) {
        if (currentId != null && !currentId.isEmpty()) {
            String[] split = currentId.split("O");
            if (split.length > 1) {
                int idNum = Integer.parseInt(split[1]);
                return "O0" + String.format("%02d", ++idNum);
            }
        }
        return "C01"; // Default starting ID
    }

    private void getQtyOnHand() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        String name = (String) cmbFabricName.getValue();
        String color = (String) cmbFabricColor.getValue();

        try {
            List<String> qtyList = OrderRepo.getQtyOnHand(name, color);

            lblQtyOnHand.setText(qtyList.get(0));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void cmbFabricOnAction(ActionEvent actionEvent) {
        getColorsForFabric();
    }

    public void cmbFabricColorOnAction(ActionEvent actionEvent) {
        getQtyOnHand();
    }

    public void txtOrderIdOnAction(ActionEvent actionEvent) throws SQLException, JRException {
        findLargerOrderIdForIdSearch(currentId1, txtOrderId.getText());
    }

    public void txtNicOnAction(ActionEvent actionEvent) throws SQLException {
        nicSearch();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        nicSearch();
    }

    private void nicSearch() throws SQLException {
        String cusNic = txtNic.getText();

        List<String> list = OrderRepo.customerSearch(cusNic);
        if (list != null) {
            lblCustomerName.setText(list.get(0));
            lblCustomerId.setText(list.get(1));

        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }

    public void cmbTailorIdOnAction(ActionEvent actionEvent) {
        getEmployeeName();
    }

    public void btnClearForNewItem(ActionEvent actionEvent) {
        removeForNewItem();
    }

    public void removeForNewItem() {
        cmbFabricName.setValue(null);
        cmbFabricColor.setValue(null);
        lblQtyOnHand.setText("");
        txtFabricSize.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtQty.setText("");
        txtMeasurements.setText("");

        txtFabricSize.setStyle("");
        txtDescription.setStyle("");
        txtMeasurements.setStyle("");
        txtPrice.setStyle("");
        txtQty.setStyle("");
    }

    public void btnIdSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String orderId2 = txtOrderId.getText();
        int lastThreeDigits1 = Integer.parseInt(currentId1.substring(1));
        int lastThreeDigits2 = Integer.parseInt(orderId2.substring(1));

        if (lastThreeDigits1 < lastThreeDigits2) {
            showErrorAlert("Order ID failed!", "Please enter order ID less than to next OrderID!.");


        } else if (lastThreeDigits1 > lastThreeDigits2) {
            searchId();

        } else {
            searchId();
        }

    }

    private void searchId () throws SQLException {
        String orderId = txtOrderId.getText();
        tblOrderCart.getItems().clear();

        ResultSet orderCartTableList = OrderRepo.getOrderCartTable(orderId);

        while (orderCartTableList.next()) {
            String description = orderCartTableList.getString(1);
            String fabricName = orderCartTableList.getString(2);
            String fabricColor = orderCartTableList.getString(3);
            String measurements = orderCartTableList.getString(4);
            double fabricSize = orderCartTableList.getDouble(5);
            double unitPrice = orderCartTableList.getDouble(6);
            int qty = orderCartTableList.getInt(7);
            double total = qty * unitPrice;

            CartTm cartTm = new CartTm(description, fabricName, fabricColor, fabricSize, measurements, unitPrice, qty, total, new JFXButton());
            obList.add(cartTm);
            tblOrderCart.setItems(obList);
        }
        lblOrderId.setText(txtOrderId.getText());

        List<String> orderJoinTablesList = OrderRepo.getOrderDetails(orderId);

        lblOrderDate.setText(orderJoinTablesList.get(0));
        returnDate.setValue(LocalDate.parse(orderJoinTablesList.get(1)));
        cmbTailorId.setValue(orderJoinTablesList.get(2));
        cmbStatus.setValue(orderJoinTablesList.get(3));
        lblPaymentId.setText(orderJoinTablesList.get(4));
        txtNic.setText(orderJoinTablesList.get(5));
        lblCustomerName.setText(orderJoinTablesList.get(6));
        lblCustomerId.setText(orderJoinTablesList.get(7));
        lblEmployeeName.setText(orderJoinTablesList.get(8));
        cmbPaymentType.setValue(orderJoinTablesList.get(9));
        lblNetTotal.setText(orderJoinTablesList.get(10));
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String orderId = lblOrderId.getText();
        String status = cmbStatus.getValue();

        boolean isUpdateStatus = OrderRepo.updateStatus(orderId, status);

        if (isUpdateStatus) {
            new Alert(Alert.AlertType.CONFIRMATION, "Order Completed!").show();
            clearFields();
        } else {
            new Alert(Alert.AlertType.WARNING, "Order Completed Unsuccessfully!").show();
        }
    }

    public void btnBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        findLargerOrderID(currentId1, txtOrderId.getText());
    }

    public void findLargerOrderID(String currentId1, String orderId1) throws JRException, SQLException {
        // Extract the last three digits from the order IDs
        int lastThreeDigits1 = Integer.parseInt(currentId1.substring(1));
        int lastThreeDigits2 = Integer.parseInt(orderId1.substring(1));

        // Compare the last three digits numerically
        if (lastThreeDigits1 < lastThreeDigits2) {
            showErrorAlert("Order ID failed!", "Please enter order ID less than to next OrderID!.");

        } else if (lastThreeDigits1 > lastThreeDigits2) {
            printBill1();
        } else {
            printBill1();
        }
    }

    private void printBill1() throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/orderBill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String, Object> data = new HashMap<>();
        data.put("orderID", txtOrderId.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint, false);
    }


    public void btnViewOrdersOnAction(ActionEvent actionEvent) throws SQLException, IOException {

        FXMLLoader customerLoader = new FXMLLoader(getClass().getResource("/view/ViewOrderForm.fxml"));
        Parent customerRoot = customerLoader.load();
        rootNode.getChildren().clear();
        rootNode.getChildren().add(customerRoot);

        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setTitle("Order view");
    }

    private void printBill() throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/orderBill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String, Object> data = new HashMap<>();
        data.put("orderID", lblOrderId.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint, false);
    }

    public void findLargerOrderIdForIdSearch(String currentId1, String orderId1) throws JRException, SQLException {
        // Extract the last three digits from the order IDs
        int lastThreeDigits1 = Integer.parseInt(currentId1.substring(1));
        int lastThreeDigits2 = Integer.parseInt(orderId1.substring(1));

        // Compare the last three digits numerically
        if (lastThreeDigits1 < lastThreeDigits2) {
            showErrorAlert("Order ID failed!", "Please enter order ID less than to next OrderID!.");

        } else if (lastThreeDigits1 > lastThreeDigits2) {
            searchId();
        } else {
            searchId();
        }
    }


}