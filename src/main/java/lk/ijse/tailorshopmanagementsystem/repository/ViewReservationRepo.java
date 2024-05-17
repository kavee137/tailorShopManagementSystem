package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;

import java.sql.*;

public class ViewReservationRepo {

    public static ResultSet getReservationDetails(Date dateFrom, Date dateTo) throws SQLException {
        String sql = "SELECT r.reservationID, c.customerID, c.NIC, c.customerName, c.customerAddress, c.customerTel, r.paymentID, r.reservationDate, r.returnDate, r.status AS reservationStatus, rd.productID, rd.qty AS quantity, rd.total AS totalAmount, p.paymentType, p.price AS paymentPrice FROM reservation AS r JOIN customer AS c ON r.customerID = c.customerID JOIN reservationDetails AS rd ON r.reservationID = rd.reservationID JOIN payment AS p ON r.paymentID = p.paymentID WHERE r.returnDate BETWEEN ? AND ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, dateFrom);
        pstm.setObject(2, dateTo);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }

    public static ResultSet getIncompleteReservationDetails() throws SQLException {
        String sql = "SELECT r.reservationID, c.customerID, c.NIC, c.customerName, c.customerAddress, c.customerTel, r.paymentID, r.reservationDate, r.returnDate, r.status AS reservationStatus, rd.productID, rd.qty AS quantity, rd.total AS totalAmount, p.paymentType, p.price AS paymentPrice FROM reservation AS r JOIN customer AS c ON r.customerID = c.customerID JOIN reservationDetails AS rd ON r.reservationID = rd.reservationID JOIN payment AS p ON r.paymentID = p.paymentID WHERE r.status = 'Incomplete'";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }

    public static ResultSet getAll() throws SQLException {
        String sql = "SELECT r.reservationID, c.customerID, c.NIC, c.customerName, c.customerAddress, c.customerTel, r.paymentID, r.reservationDate, r.returnDate, r.status AS reservationStatus, rd.productID, rd.qty AS quantity, rd.total AS totalAmount, p.paymentType, p.price AS paymentPrice FROM reservation AS r JOIN customer AS c ON r.customerID = c.customerID JOIN reservationDetails AS rd ON r.reservationID = rd.reservationID JOIN payment AS p ON r.paymentID = p.paymentID";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }

    public static ResultSet getCompleted() throws SQLException {
        String sql = "SELECT r.reservationID, c.customerID, c.NIC, c.customerName, c.customerAddress, c.customerTel, r.paymentID, r.reservationDate, r.returnDate, r.status AS reservationStatus, rd.productID, rd.qty AS quantity, rd.total AS totalAmount, p.paymentType, p.price AS paymentPrice FROM reservation AS r JOIN customer AS c ON r.customerID = c.customerID JOIN reservationDetails AS rd ON r.reservationID = rd.reservationID JOIN payment AS p ON r.paymentID = p.paymentID WHERE r.status = 'Completed'";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }
}
