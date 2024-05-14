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
import lk.ijse.tailorshopmanagementsystem.Util.Regex;
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
    private Label lblProductId;
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
            lblProductId.setText(String.valueOf(selectedUser.getProductID()));
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
            lblProductId.setText(String.valueOf(nextProductId));

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

        String id = lblProductId.getText();
        String name = txtName.getText();
        String color = txtColor.getText();
        String size = txtSize.getText();
        String price = txtUnitPrice.getText();
        String qtyOnHand = txtQtyOnHand.getText();


        Product product = new Product(id, name, color, size, price, qtyOnHand);

        if (isValied()) {
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
    void btnSaveOnAction(ActionEvent event) {

        String id = lblProductId.getText();
        String name = txtName.getText();
        String color = txtColor.getText();
        String size = txtSize.getText();
        String price = txtUnitPrice.getText();
        String qtyOnHand = txtQtyOnHand.getText();

        Product product = new Product(id, name, color, size, price, qtyOnHand);
        if (isValied()) {
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
        } else {
            // Show error message if validation fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please fill in all fields correctly.");
            alert.showAndWait();
        }
    }

    public boolean isValied() {
        boolean nameValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
        boolean color = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtColor);
        boolean size = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.QTY, txtSize);
        boolean unitPrice = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.PRICEDOT, txtUnitPrice);
        boolean qtyOnHand = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.QTY, txtQtyOnHand);

        return nameValid && color && size && unitPrice && qtyOnHand;
    }

    public void sizeKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.FABSIZE, txtSize);
    }

    public void colorKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtColor);
    }

    public void priceKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.PRICEDOT, txtUnitPrice);
    }

    public void nameKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
    }

    public void qtyOnHandKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.QTY, txtQtyOnHand);
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

        if (cmbProductNameValue != null && cmbProductColorValue != null && cmbProductSizeValue != null) {

            txtName.setText(cmbProductNameValue);
            txtColor.setText(cmbProductColorValue);
            txtSize.setText(cmbProductSizeValue);

            Product1 product1 = ProductRepo.productSearch(cmbProductNameValue, cmbProductColorValue, cmbProductSizeValue);
            if (product1 != null) {
                lblProductId.setText(product1.getProductID());
                txtUnitPrice.setText(product1.getUnitPrice());
                txtQtyOnHand.setText(product1.getQtyOnHand());
            }

        } else {
            // Handle the case where one or more values are null
            showErrorAlert("Product details Failed", "Please select product details!");
        }



    }

    private void showErrorAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = lblProductId.getText();

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

        txtUnitPrice.setStyle(""); // Set text color to default
        txtQtyOnHand.setStyle(""); // Set text color to default
        txtSize.setStyle(""); // Set text color to default
        txtName.setStyle(""); // Set text color to default
        txtColor.setStyle(""); // Set text color to default
        lblProductId.setStyle(""); // Set text color to default

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
