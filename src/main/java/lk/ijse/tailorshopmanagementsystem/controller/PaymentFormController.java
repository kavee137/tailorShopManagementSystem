package lk.ijse.tailorshopmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.tailorshopmanagementsystem.model.User;
import lk.ijse.tailorshopmanagementsystem.model.tm.PaymentTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.UserTm;
import lk.ijse.tailorshopmanagementsystem.repository.PaymentRepo;
import lk.ijse.tailorshopmanagementsystem.repository.UserRepo;

import java.sql.SQLException;
import java.util.List;

public class PaymentFormController {
    @FXML
    private TableView<PaymentTm> tblPayment;
    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colPaymentId;

    @FXML
    private TableColumn<?, ?> colPaymentType;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colReservationId;


    public void initialize() {
        setCellValueFactory();
        loadPaymentDetails();
    }

    private void setCellValueFactory() {
        colReservationId.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        colPaymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void loadPaymentDetails() {
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();

        try {
            List<PaymentTm> pList = PaymentRepo.getDetails();
            for (PaymentTm pd : pList) {
                PaymentTm tm = new PaymentTm(
                        pd.getReservationID(),
                        pd.getOrderID(),
                        pd.getPaymentID(),
                        pd.getPaymentType(),
                        pd.getPrice()
                );

                obList.add(tm);
            }

            tblPayment.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





}
