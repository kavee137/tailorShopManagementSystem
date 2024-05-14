package lk.ijse.tailorshopmanagementsystem.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.tailorshopmanagementsystem.Util.Regex;
import lk.ijse.tailorshopmanagementsystem.model.Customer;
import lk.ijse.tailorshopmanagementsystem.model.Fabric;
import lk.ijse.tailorshopmanagementsystem.model.Product1;
import lk.ijse.tailorshopmanagementsystem.model.Supplier;
import lk.ijse.tailorshopmanagementsystem.model.tm.CustomerTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.FabricTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.SupplierTm;
import lk.ijse.tailorshopmanagementsystem.repository.*;

import java.sql.SQLException;
import java.util.List;

public class FabricFormController {
    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private JFXComboBox<String> cmbSupplierId;

    @FXML
    private JFXComboBox<String> cmbFabricColor;

    @FXML
    private JFXComboBox<String> cmbFabricName;

    @FXML
    private TableColumn<?, ?> colFabricColor;

    @FXML
    private TableColumn<?, ?> colFabricId;

    @FXML
    private TableColumn<?, ?> colFabricName;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private Label lblSupplierId;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<FabricTm> tblFabric;

    @FXML
    private TextField txtColor;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtQtyOnHand;




    public void initialize() {
        getFabricName();
        getCurrentFabricId();
        loadAllSupplierId();
        setCellValueFactory();
        loadAllFabric();
        showSelectedUserDetails();
    }

    private void showSelectedUserDetails() {
        FabricTm selectedUser = tblFabric.getSelectionModel().getSelectedItem();
        tblFabric.setOnMouseClicked(event -> showSelectedUserDetails());
        if (selectedUser != null) {
            txtId.setText(selectedUser.getFabricID());
            txtName.setText(selectedUser.getFabricName());
            cmbSupplierId.setValue(selectedUser.getSupplierID());
            txtColor.setText(selectedUser.getFabricColor());
            txtQtyOnHand.setText(String.valueOf(selectedUser.getFabricQtyOnHand()));
        }
    }

    private void setCellValueFactory() {
        colFabricId.setCellValueFactory(new PropertyValueFactory<>("fabricID"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        colFabricName.setCellValueFactory(new PropertyValueFactory<>("fabricName"));
        colFabricColor.setCellValueFactory(new PropertyValueFactory<>("fabricColor"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("fabricQtyOnHand"));
    }

    private void loadAllFabric() {
        ObservableList<FabricTm> obList = FXCollections.observableArrayList();

        try {
            List<FabricTm> fabricList = FabricRepo.getAll();
            for (FabricTm fabric : fabricList) {
                FabricTm tm = new FabricTm(
                        fabric.getFabricID(),
                        fabric.getSupplierID(),
                        fabric.getFabricName(),
                        fabric.getFabricColor(),
                        fabric.getFabricQtyOnHand()
                );

                obList.add(tm);
            }

            tblFabric.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllSupplierId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> idList = SupplierRepo.getSuppliereIds();

            for (String code : idList) {
                obList.add(code);
            }
            cmbSupplierId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentFabricId() {
        try {
            String currentId = FabricRepo.getCurrentId();

            String nextOrderId = generateNextFabricId(currentId);
            txtId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextFabricId(String currentId) {
        if (currentId != null && !currentId.isEmpty()) {
            String[] split = currentId.split("F");
            if (split.length > 1) {
                int idNum = Integer.parseInt(split[1]);
                return "F" + String.format("%02d", ++idNum);
            }
        }
        return "F01"; // Default starting ID
    }

    private void getFabricName() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nameList = FabricRepo.getFabricName();

            for (String code : nameList) {
                obList.add(code);
            }
            cmbFabricName.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
        getCurrentFabricId();
    }

    private void clearFields() {
        cmbFabricName.setValue(null);
        cmbFabricColor.setValue(null);
        cmbSupplierId.setValue(null);
        txtId.setText("");
        txtName.setText("");
        txtColor.setText("");
        txtQtyOnHand.setText("");

        txtId.setStyle("");
        txtName.setStyle("");
        txtColor.setStyle("");
        txtQtyOnHand.setStyle("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            boolean isDeleted = FabricRepo.delete(id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Fabric deleted!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (isValied() && (cmbSupplierId.getValue() != null && !cmbSupplierId.getValue().toString().isEmpty())) {

            String id = txtId.getText();
            String supId = cmbSupplierId.getValue();
            String name = txtName.getText();
            String color = txtColor.getText();
            int qty = Integer.parseInt(txtQtyOnHand.getText());

            Fabric fabric = new Fabric(id, supId, name, color, qty);

            try {
                boolean isSaved = FabricRepo.save(fabric);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Fabric saved!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            // Show error message if validation fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please fill in all fields correctly.");
            alert.showAndWait();
        }
    }

    public boolean isValied(){
        boolean nameValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
        boolean color = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtColor);
        boolean qtyOnHand = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.QTY, txtQtyOnHand);
        boolean idValid = Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.FABID, txtId);

        return nameValid && color && qtyOnHand && idValid ;
    }

    public void nameKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtName);
    }

    public void idKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.FABID, txtId);
    }

    public void colorKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.NAME, txtColor);
    }

    public void qtyOnHandKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.tailorshopmanagementsystem.Util.TextField.QTY, txtQtyOnHand);
    }
    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String fabName = (String) cmbFabricName.getValue();
        String  fabColor = (String) cmbFabricColor.getValue();

        txtName.setText(fabName);
        txtColor.setText(fabColor);

        Fabric fabricSearch = FabricRepo.fabricSearch(fabName, fabColor);
        if (fabricSearch != null) {
            txtId.setText(fabricSearch.getFabricID());
            cmbSupplierId.setValue(fabricSearch.getSupplierID());
            txtName.setText(fabricSearch.getFabricName());
            txtColor.setText(fabricSearch.getFabricColor());
            txtQtyOnHand.setText(String.valueOf(fabricSearch.getFabricQtyOnHand()));
        }else {
            new Alert(Alert.AlertType.INFORMATION, "Fabric not found!").show();
        }
    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        if (isValied() && (cmbSupplierId.getValue() != null && !cmbSupplierId.getValue().toString().isEmpty())) {
            String id = txtId.getText();
            String supId = cmbSupplierId.getValue();
            String name = txtName.getText();
            String color = txtColor.getText();
            int qty = Integer.parseInt(txtQtyOnHand.getText());

            Fabric fabric = new Fabric(id, supId, name, color, qty);

            try {
                boolean isUpdated = FabricRepo.update(fabric);
                if(isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Fabric updated!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }

        }else {
            // Show error message if validation fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please fill in all fields correctly.");
            alert.showAndWait();
        }
    }
    @FXML
    void cmbFabricNameOnAction(ActionEvent event) {
        getColorsForFabric();
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
}
