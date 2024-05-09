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
import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.Order;
import lk.ijse.tailorshopmanagementsystem.model.OrderDetails;
import lk.ijse.tailorshopmanagementsystem.model.PlaceOrder;
import lk.ijse.tailorshopmanagementsystem.model.tm.CartTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.ReservationTm;
import lk.ijse.tailorshopmanagementsystem.repository.OrderRepo;
import lk.ijse.tailorshopmanagementsystem.repository.PlaceOrderRepo;
import lk.ijse.tailorshopmanagementsystem.repository.ReservationRepo;


import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        // Call the method to initialize validation for text fields
        initializeValidation();
    }

    //    ------------------------ Validation Part  ---------------------------------

    private void initializeValidation() {
            addValidationListener(txtNic, "[0-9]*V?", true); // Numbers and 'V' (upper case)
            addValidationListener(txtQty, "\\d+", true); // Only numbers
            addValidationListener(txtOrderId, "O\\d*", true); // Only 'O' followed by numbers
            addValidationListener(txtFabricSize, "\\d*\\.?\\d*", true); // Only numbers and dot '.'
            addValidationListener(txtPrice, "(?:\\d*\\.\\d+)|(?:\\d+\\.?)", true); // Numbers and dot '.'
            addNullValidationListener(txtDescription); // Can't be null, show red if null
            addNullValidationListener(txtMeasurements); // Can't be null, show red if null
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

    private void addNullValidationListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // If text is null or empty, show red border
                textField.setStyle("-fx-border-color: red;");
            } else {
                // If text is not null or empty, show default border color
                textField.setStyle("-fx-border-color: #3498db;");
            }
        });
    }


//    ------------------------ End of Validation Part  ---------------------------------


    void setCmbStatus() {
        ObservableList<String> status = FXCollections.observableArrayList();
        cmbStatus.setValue("processing");
        status.add("processing");
        status.add("completed");

        cmbStatus.setItems(status);
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws SQLException {

        String orderId = txtOrderId.getText();
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


            List<String> fabId = getFabricId(obList.get(i).getFabricName(), obList.get(i).getFabricColor());
            System.out.println(fabId);

            OrderDetails od = new OrderDetails(
                    orderId,
                    fabId.get(0),
                    tm.getDescription(),
                    tm.getMeasurements(),
                    tm.getFabricSize(),
                    tm.getUnitPrice(),
                    tm.getQty()

            );

            odList.add(od);
        }


        PlaceOrder po = new PlaceOrder(order, odList);
        try {
            boolean isPlaced = PlaceOrderRepo.placeOrder(po, paymentType, netTotal);
            if (isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Order Placed Unsuccessfully!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private List<String> getFabricId(String name, String color) throws SQLException {
        String sql = "SELECT fabricID FROM fabric WHERE fabricName = ? AND fabricColor = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);
        pstm.setObject(2, color);


        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }

        return idList;

    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String description = txtDescription.getText();
        String fabricName = (String) cmbFabricName.getValue();
        String fabricColor = (String) cmbFabricColor.getValue();
        double fabricSize = Double.parseDouble((txtFabricSize.getText()));
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

    private void getCurrentOrderId() {
        try {
            String currentId = OrderRepo.getCurrentId();

            String nextOrderId = generateNextOrderId(currentId);
            txtOrderId.setText(nextOrderId);

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

    public void lblNicOnAction(ActionEvent actionEvent) {
//        getSuggestions(String.valueOf(txtNic));
    }


    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String cusNic = txtNic.getText();

        List<String> list = OrderRepo.customerSearch(cusNic);
        if (list != null) {
            lblCustomerName.setText(list.get(0));
            lblCustomerId.setText(list.get(1));

        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }

    public void btnAddNewCustomerOnAction(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer_form.fxml"));
        Parent load = loader.load();
        rootNode.getChildren().clear();
        rootNode.getChildren().add(load);

        // Get the stage from any node
        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setTitle("Customer Manage");
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
    }

    public void btnIdSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String orderId = txtOrderId.getText();
        tblOrderCart.getItems().clear();

        ResultSet orderCartTableList = OrderRepo.getOrderCartTable(orderId);

//        | description | fabricName | fabricColor | measurements      | fabricSize | unitPrice | qty  |

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

        List<String> orderJoinTablesList = OrderRepo.getOrderDetails(orderId);
//SELECT o.orderDate, o.returnDate, o.employeeID, o.status, o.paymentID, c.NIC, c.customerName, c.customerID, e.employeeName, p.paymentType, p.price


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
        String orderId = txtOrderId.getText();
        String status = cmbStatus.getValue();

        boolean isUpdateStatus = OrderRepo.updateStatus(orderId, status);

        if (isUpdateStatus) {
            new Alert(Alert.AlertType.CONFIRMATION, "Order Completed!").show();
            clearFields();
        } else {
            new Alert(Alert.AlertType.WARNING, "Order Completed Unsuccessfully!").show();
        }
    }
}