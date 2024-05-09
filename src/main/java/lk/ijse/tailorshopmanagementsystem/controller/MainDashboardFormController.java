package lk.ijse.tailorshopmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.tailorshopmanagementsystem.repository.*;

import java.sql.SQLException;

public class MainDashboardFormController {
    public Label lblEmployeeCount;
    public Label lblOrderCount;
    public Label lblReservationCount;
    public Label lblTransactionCount;
    public Label lblProductCount;
    public Label lblSupplierCount;
    public Label lblUserCount;
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
    }



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
