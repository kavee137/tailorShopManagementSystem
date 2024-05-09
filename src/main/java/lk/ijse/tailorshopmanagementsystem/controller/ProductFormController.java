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
import lk.ijse.tailorshopmanagementsystem.model.Customer;
import lk.ijse.tailorshopmanagementsystem.model.Product;
import lk.ijse.tailorshopmanagementsystem.model.Product1;
import lk.ijse.tailorshopmanagementsystem.model.tm.CustomerTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.ProductTm;
import lk.ijse.tailorshopmanagementsystem.repository.CustomerRepo;
import lk.ijse.tailorshopmanagementsystem.repository.ProductRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductFormController {
    @FXML
    public TextField txtColor;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?>colColor;
    @FXML
    private TableColumn<?, ?> colSize;
    @FXML
    private TableColumn<?, ?> colUnitPrice;
    @FXML
    private TableColumn<?, ?> colQtyOnHand;
    @FXML
    private TableView<ProductTm> tblProduct;
    @FXML
    private TextField txtSize;
    @FXML
    private TextField txtName;
    @FXML
    private JFXComboBox <String> cmbProductSize;
    @FXML
    private JFXComboBox <String> cmbProductColor;
    @FXML
    private JFXComboBox <String> cmbProductName;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtQtyOnHand;
    @FXML
    private TextField txtUnitPrice;

    public AnchorPane rootNode;





    public void initialize() {
        setCellValueFactory();
        loadAllProducts();
        getCurrentProductId();
        getProductName();
        showSelectedProductDetails();

        initializeValidation();
    }




    private void initializeValidation() {
        addValidationListener(txtId, "\\d+", true); // Numbers only
        addValidationListener(txtName, "[a-zA-Z]+", true); // Letters only
        addValidationListener(txtColor, "[a-zA-Z]+", true); // Letters only
        addValidationListener(txtSize, "\\d+", true); // Numbers only
        addValidationListener(txtUnitPrice, "\\d+", true); // Numbers only
        addValidationListener(txtQtyOnHand, "\\d+", true); // Numbers only
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











    private void showSelectedProductDetails() {
        ProductTm selectedUser = tblProduct.getSelectionModel().getSelectedItem();
        tblProduct.setOnMouseClicked(event -> showSelectedProductDetails());
        if (selectedUser != null) {
            cmbProductName.setValue(selectedUser.getProductName());
            cmbProductColor.setValue(selectedUser.getProductColor());
            cmbProductSize.setValue(selectedUser.getProductSize());
            txtName.setText(selectedUser.getProductName());
            txtColor.setText(selectedUser.getProductColor());
            txtSize.setText(selectedUser.getProductSize());
            txtId.setText(String.valueOf(selectedUser.getProductID()));
            txtUnitPrice.setText(String.valueOf(selectedUser.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(selectedUser.getQtyOnHand()));
        }
    }

    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("productID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("productColor"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("productSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
    }

    private void loadAllProducts() {
        ObservableList<ProductTm> obList = FXCollections.observableArrayList();

        try {
            List<ProductTm> productList = ProductRepo.getAll();
            for (ProductTm product : productList) {
                ProductTm t = new ProductTm(
                        product.getProductID(),
                        product.getProductName(),
                        product.getProductColor(),
                        product.getProductSize(),
                        product.getUnitPrice(),
                        product.getQtyOnHand()
                );
//                System.out.println(tm);
                obList.add(t);
            }

            tblProduct.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getProductSize() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        String color = (String) cmbProductColor.getValue();
        String name = (String) cmbProductName.getValue();

        try {
            List<String> sizeList = ProductRepo.getProductSize(color, name);

            for (String size : sizeList) {
                obList.add(size);
            }
            cmbProductSize.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getProductName() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = ProductRepo.getProductName();

            for (String code : codeList) {
                obList.add(code);
            }
            cmbProductName.setItems(obList);
            String productName = (String) cmbProductName.getValue();



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void getProductColor() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        String name = (String) cmbProductName.getValue();

        try {
            List<String> colorList = ProductRepo.getProductColor(name);

            for (String code : colorList) {
                obList.add(code);
            }
            cmbProductColor.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentProductId() {
        try {
            int currentId = ProductRepo.getCurrentId();

            int nextProductId = Integer.parseInt(String.valueOf(generateNextProductId(currentId)));
            txtId.setText(String.valueOf(nextProductId));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int generateNextProductId(int currentId) {
        if(currentId != 0) {
                return ++currentId;
        }
        return 1;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        String id = txtId.getText();
        String name = txtName.getText();
        String color = txtColor.getText();
        String size = txtSize.getText();
        String price = txtUnitPrice.getText();
        String qtyOnHand = txtQtyOnHand.getText();

        Product product = new Product(id, name, color, size, price, qtyOnHand);
        try {
            boolean isUpdated = ProductRepo.update(product);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "product updated!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String color = txtColor.getText();
        String size = txtSize.getText();
        String price = txtUnitPrice.getText();
        String qtyOnHand = txtQtyOnHand.getText();

        Product product = new Product(id, name, color, size, price, qtyOnHand);

        try {
            boolean isSaved = ProductRepo.save(product);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "product saved!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        Stage stage = (Stage) rootNode.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String cmbProductNameValue = (String) cmbProductName.getValue();
        String cmbProductColorValue = (String) cmbProductColor.getValue();
        String cmbProductSizeValue = (String) cmbProductSize.getValue();

        txtName.setText(cmbProductNameValue);
        txtColor.setText(cmbProductColorValue);
        txtSize.setText(cmbProductSizeValue);

        Product1 product1 = ProductRepo.productSearch(cmbProductNameValue, cmbProductColorValue, cmbProductSizeValue);
        if (product1 != null) {
            txtId.setText(product1.getProductID());
            txtUnitPrice.setText(product1.getUnitPrice());
            txtQtyOnHand.setText(product1.getQtyOnHand());
        }else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            boolean isDeleted = ProductRepo.delete(id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "product deleted!").show();
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

        setCellValueFactory();
        loadAllProducts();
        getCurrentProductId();
        getProductName();
    }

    private void clearFields() {
        cmbProductName.setValue(null);
        cmbProductName.setValue(null);
        cmbProductSize.setValue(null);
        txtUnitPrice.setText("");
        txtQtyOnHand.setText("");
        txtSize.setText("");
        txtName.setText("");
        txtColor.setText("");
    }

    public void cmbProductNameOnAction(ActionEvent actionEvent) {
        getProductColor();
    }

    public void cmbProductColorOnAction(ActionEvent actionEvent) {
        getProductSize();
    }

    public void cmbProductSizeOnAction(ActionEvent actionEvent) {

    }
}
