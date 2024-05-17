package lk.ijse.tailorshopmanagementsystem.controller;

import com.jfoenix.controls.JFXButton;
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
import lk.ijse.tailorshopmanagementsystem.model.tm.ReservationTm;
import lk.ijse.tailorshopmanagementsystem.model.tm.ReservationViewTm;
import lk.ijse.tailorshopmanagementsystem.repository.ReservationRepo;
import lk.ijse.tailorshopmanagementsystem.repository.ViewReservationRepo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ViewReservationFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colCusId;

    @FXML
    private TableColumn<?, ?> colCusName;

    @FXML
    private TableColumn<?, ?> colNic;

    @FXML
    private TableColumn<?, ?> colPId;

    @FXML
    private TableColumn<?, ?> colPType;

    @FXML
    private TableColumn<?, ?> colPayId;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colResDate;

    @FXML
    private TableColumn<?, ?> colResId;

    @FXML
    private TableColumn<?, ?> colRetDate;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private DatePicker dpDateFrom;

    @FXML
    private DatePicker dpDateTo;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<ReservationViewTm> tblReservationView;

    private ObservableList<ReservationViewTm> obList = FXCollections.observableArrayList();



    public void initialize() throws SQLException {
        setCellValueFactory();
        loadAllIncompleteReservation();
    }

    private void setCellValueFactory(){
        colResId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        colCusId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colPayId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colResDate.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        colRetDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colPType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("paymentPrice"));
    }





    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException {
        dpDateTo.setValue(null);
        dpDateFrom.setValue(null);

        initialize();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {

        if (dpDateFrom.getValue() != null && dpDateTo.getValue() != null) {

            Date dateFrom = Date.valueOf(dpDateFrom.getValue());
            Date dateTo = Date.valueOf(dpDateTo.getValue());

            tblReservationView.getItems().clear();

            ResultSet reservationDetails = ViewReservationRepo.getReservationDetails(dateFrom, dateTo);

            while (reservationDetails.next()) {
                int resId = reservationDetails.getInt(1);
                String cusId = reservationDetails.getString(2);
                String nic = reservationDetails.getString(3);
                String cusName = reservationDetails.getString(4);
                String cusAddress = reservationDetails.getString(5);
                String tel = reservationDetails.getString(6);
                int payId = reservationDetails.getInt(7);
                Date resDate = reservationDetails.getDate(8);
                Date retDate = reservationDetails.getDate(9);
                String status = reservationDetails.getString(10);
                int productId = reservationDetails.getInt(11);
                int qty = reservationDetails.getInt(12);
                double total = reservationDetails.getDouble(13);
                String pType = reservationDetails.getString(14);
                double price = reservationDetails.getDouble(15);

                ReservationViewTm r = new ReservationViewTm(resId, cusId, nic, cusName, cusAddress, tel, payId, resDate, retDate, status, productId, qty, total, pType, price);
                obList.add(r);
                tblReservationView.setItems(obList);
            }


        } else {
            // Show error message if validation fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Reservation Search error");
            alert.setHeaderText("Reservartion search Failed");
            alert.setContentText("Please select return date period!");
            alert.showAndWait();
        }

    }




    public void loadAllIncompleteReservation() throws SQLException {
//        Date dateFrom = Date.valueOf(dpDateFrom.getValue());
//        Date dateTo = Date.valueOf(dpDateTo.getValue());



//        if (currentReservationIdForBillMethod >= nicFieldId) {

        tblReservationView.getItems().clear();

        ResultSet reservationDetails = ViewReservationRepo.getIncompleteReservationDetails();

        while (reservationDetails.next()) {
            int resId = reservationDetails.getInt(1);
            String cusId = reservationDetails.getString(2);
            String nic = reservationDetails.getString(3);
            String cusName = reservationDetails.getString(4);
            String cusAddress = reservationDetails.getString(5);
            String tel = reservationDetails.getString(6);
            int payId = reservationDetails.getInt(7);
            Date resDate = reservationDetails.getDate(8);
            Date retDate = reservationDetails.getDate(9);
            String status = reservationDetails.getString(10);
            int productId = reservationDetails.getInt(11);
            int qty = reservationDetails.getInt(12);
            double total = reservationDetails.getDouble(13);
            String pType = reservationDetails.getString(14);
            double price = reservationDetails.getDouble(15);




            ReservationViewTm r = new ReservationViewTm(resId, cusId, nic, cusName, cusAddress, tel, payId, resDate, retDate, status, productId, qty, total, pType, price);
            obList.add(r);
            tblReservationView.setItems(obList);
        }


//        } else {
//            // Show error message if validation fails
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("ReservationID error");
//            alert.setHeaderText("Reservartion search Failed");
//            alert.setContentText("Please enter valid reservation ID!");
//            alert.showAndWait();
//        }

    }

    public void btnShowAllOnAction(ActionEvent actionEvent) throws SQLException {

        tblReservationView.getItems().clear();

        ResultSet reservationDetails = ViewReservationRepo.getAll();

        while (reservationDetails.next()) {
            int resId = reservationDetails.getInt(1);
            String cusId = reservationDetails.getString(2);
            String nic = reservationDetails.getString(3);
            String cusName = reservationDetails.getString(4);
            String cusAddress = reservationDetails.getString(5);
            String tel = reservationDetails.getString(6);
            int payId = reservationDetails.getInt(7);
            Date resDate = reservationDetails.getDate(8);
            Date retDate = reservationDetails.getDate(9);
            String status = reservationDetails.getString(10);
            int productId = reservationDetails.getInt(11);
            int qty = reservationDetails.getInt(12);
            double total = reservationDetails.getDouble(13);
            String pType = reservationDetails.getString(14);
            double price = reservationDetails.getDouble(15);




            ReservationViewTm r = new ReservationViewTm(resId, cusId, nic, cusName, cusAddress, tel, payId, resDate, retDate, status, productId, qty, total, pType, price);
            obList.add(r);
            tblReservationView.setItems(obList);
        }



    }

    public void btnShowCompletedOnAction(ActionEvent actionEvent) throws SQLException {

        tblReservationView.getItems().clear();

        ResultSet reservationDetails = ViewReservationRepo.getCompleted();

        while (reservationDetails.next()) {
            int resId = reservationDetails.getInt(1);
            String cusId = reservationDetails.getString(2);
            String nic = reservationDetails.getString(3);
            String cusName = reservationDetails.getString(4);
            String cusAddress = reservationDetails.getString(5);
            String tel = reservationDetails.getString(6);
            int payId = reservationDetails.getInt(7);
            Date resDate = reservationDetails.getDate(8);
            Date retDate = reservationDetails.getDate(9);
            String status = reservationDetails.getString(10);
            int productId = reservationDetails.getInt(11);
            int qty = reservationDetails.getInt(12);
            double total = reservationDetails.getDouble(13);
            String pType = reservationDetails.getString(14);
            double price = reservationDetails.getDouble(15);

            ReservationViewTm r = new ReservationViewTm(resId, cusId, nic, cusName, cusAddress, tel, payId, resDate, retDate, status, productId, qty, total, pType, price);
            obList.add(r);
            tblReservationView.setItems(obList);
        }



    }





}
