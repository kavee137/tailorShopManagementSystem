package lk.ijse.tailorshopmanagementsystem.repository;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.Order;
import lk.ijse.tailorshopmanagementsystem.model.Reservation;
import lk.ijse.tailorshopmanagementsystem.model.ReservationDetails;
import lk.ijse.tailorshopmanagementsystem.model.tm.ReservationTm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepo {


    public static String getReservationCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS incompleteReservationCount FROM reservation WHERE status = 'Incomplete'";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String count = resultSet.getString(1);
            return count;
        }
        return null;
    }

    public static boolean updateStatus(String  resId) throws SQLException {

        String sql = "UPDATE reservation SET status = 'Completed' WHERE reservationID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, resId);

        return pstm.executeUpdate() > 0;
    }

    public static boolean returnUpdateQty(int  proId, int qty) throws SQLException {
        String sql = "UPDATE product SET qtyOnHand = qtyOnHand + ? WHERE productID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setInt(1, qty);
        pstm.setInt(2, proId);

        return pstm.executeUpdate() > 0;
    }

    public static ResultSet getReservationDetailsTable(int id) throws SQLException {
        String sql = "SELECT rd.productID,  p.productName,  p.productColor, p.unitPrice, rd.qty FROM reservation r JOIN reservationDetails rd ON r.reservationID = rd.reservationID JOIN product p ON rd.productID = p.productID WHERE rd.reservationID = ?;";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }

    public static List<String> getReservationJoinTable(int id) throws SQLException {
        String sql = "SELECT r.customerID, c.customerName, r.paymentID, p.paymentType, p.price, r.reservationDate, r.returnDate, r.status AS reservationStatus, c.NIC FROM reservation r JOIN payment p ON r.paymentID = p.paymentID JOIN customer c ON r.customerID = c.customerID WHERE reservationID = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();

        ArrayList<String> rowData = new ArrayList<>();

        if (resultSet.next()) {
            rowData.add(resultSet.getString(1));
            rowData.add(resultSet.getString(2));
            rowData.add(resultSet.getString(3));
            rowData.add(resultSet.getString(4));
            rowData.add(resultSet.getString(5));
            rowData.add(resultSet.getString(6));
            rowData.add(resultSet.getString(7));
            rowData.add(resultSet.getString(8));
            rowData.add(resultSet.getString(9));
        }

        return rowData;
    }


    public static boolean save(Reservation reservation) throws SQLException {

        String sql = "INSERT INTO reservation VALUES(?, ?, ?, ?, ? ,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, reservation.getReservationID());
        pstm.setString(2, reservation.getCustomerID());
        pstm.setInt(3, reservation.getPaymentID());
        pstm.setDate(4, reservation.getReservationDate());
        pstm.setDate(5, reservation.getReturnDate());
        pstm.setString(6, reservation.getStatus());


        return pstm.executeUpdate() > 0;
    }

    public static List<String> getProductSize(String productName, String productColor) throws SQLException {
        String sql = "SELECT productSize FROM product WHERE productName = ? AND productColor = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, productName);
        pstm.setObject(2, productColor);


        ResultSet resultSet = pstm.executeQuery();
        List<String> codeList = new ArrayList<>();
        while (resultSet.next()) {
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }

    public static int getCurrentPaymentId() throws SQLException {
        String sql = "SELECT paymentID FROM payment ORDER BY paymentID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            int paymentId = resultSet.getInt(1);
            return paymentId;
        }
        return 1;
    }

    public static  List<String> getQtyOnHand(String name, String color, String size) throws SQLException {
        String sql = "SELECT qtyOnHand, productID, unitPrice FROM product WHERE productName = ? AND productColor = ? AND productSize = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);
        pstm.setObject(2, color);
        pstm.setObject(3, size);


        ResultSet resultSet = pstm.executeQuery();
        List<String> qty = new ArrayList<>();
        while (resultSet.next()) {
            qty.add(resultSet.getString(1));
            qty.add(resultSet.getString(2));
            qty.add(resultSet.getString(3));
        }
        return qty;
    }

    public static List<String> getProductColors(String productName) throws SQLException {
        String sql = "SELECT DISTINCT productColor FROM product WHERE productName = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, productName);


        ResultSet resultSet = pstm.executeQuery();
        List<String> codeList = new ArrayList<>();
        while (resultSet.next()) {
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }

    public static List<String> getProductName() throws SQLException {
        String sql = "SELECT DISTINCT productName FROM product";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> codeList = new ArrayList<>();
        while (resultSet.next()) {
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }

    public static List<String> customerSearch(String cusNic) throws SQLException {
        String sql = "SELECT customerName, customerID FROM customer WHERE NIC = ? AND status = ?";
        String status = "Active";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, cusNic);
        pstm.setObject(2, status);

        List<String> a = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            a.add(resultSet.getString(1));
            a.add(resultSet.getString(2));

            return a;
        }

        return null;
    }

    public static int getCurrentId() throws SQLException {
        String sql = "SELECT reservationID FROM reservation ORDER BY reservationID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            int resID = resultSet.getInt(1);
            return resID;
        }
        return 1;
    }
}
