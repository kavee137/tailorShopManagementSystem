package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;

import java.sql.*;

public class ViewOrderRepo {


    public static ResultSet getOrderDetails(Date dateFrom, Date dateTo) throws SQLException {
        String sql = "SELECT o.orderID, p.paymentID, c.NIC, o.orderDate, o.returnDate, o.status AS orderStatus, c.customerName, c.customerAddress, c.customerTel , o.employeeID,  e.employeeName, od.fabricID, f.fabricName, f.fabricColor,od.description, od.measurements, od.fabricSize, od.unitPrice, od.qty, od.total, p.paymentType, p.price AS paymentPrice FROM customer c JOIN orders o ON c.customerID = o.customerID JOIN employee e ON o.employeeID = e.employeeID JOIN orderDetails od ON o.orderID = od.orderID JOIN fabric f ON od.fabricID = f.fabricID JOIN payment p ON o.paymentID = p.paymentID WHERE o.returnDate BETWEEN ? AND ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, dateFrom);
        pstm.setObject(2, dateTo);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }










    public static ResultSet getAll() throws SQLException {
        String sql = "SELECT o.orderID, p.paymentID, c.NIC, o.orderDate, o.returnDate, o.status AS orderStatus, c.customerName, c.customerAddress, c.customerTel , o.employeeID,  e.employeeName, od.fabricID, f.fabricName, f.fabricColor,od.description, od.measurements, od.fabricSize, od.unitPrice, od.qty, od.total, p.paymentType, p.price AS paymentPrice FROM customer c JOIN orders o ON c.customerID = o.customerID JOIN employee e ON o.employeeID = e.employeeID JOIN orderDetails od ON o.orderID = od.orderID JOIN fabric f ON od.fabricID = f.fabricID JOIN payment p ON o.paymentID = p.paymentID";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }









    public static ResultSet getCompleted() throws SQLException {
        String sql = "SELECT o.orderID, p.paymentID, c.NIC, o.orderDate, o.returnDate, o.status AS orderStatus, c.customerName, c.customerAddress, c.customerTel , o.employeeID,  e.employeeName, od.fabricID, f.fabricName, f.fabricColor,od.description, od.measurements, od.fabricSize, od.unitPrice, od.qty, od.total, p.paymentType, p.price AS paymentPrice FROM customer c JOIN orders o ON c.customerID = o.customerID JOIN employee e ON o.employeeID = e.employeeID JOIN orderDetails od ON o.orderID = od.orderID JOIN fabric f ON od.fabricID = f.fabricID JOIN payment p ON o.paymentID = p.paymentID WHERE o.status = 'completed'";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }





    public static ResultSet getProcessingOrderDetails() throws SQLException {
        String sql = "SELECT o.orderID, p.paymentID, c.NIC, o.orderDate, o.returnDate, o.status AS orderStatus, c.customerName, c.customerAddress, c.customerTel , o.employeeID,  e.employeeName, od.fabricID, f.fabricName, f.fabricColor,od.description, od.measurements, od.fabricSize, od.unitPrice, od.qty, od.total, p.paymentType, p.price AS paymentPrice FROM customer c JOIN orders o ON c.customerID = o.customerID JOIN employee e ON o.employeeID = e.employeeID JOIN orderDetails od ON o.orderID = od.orderID JOIN fabric f ON od.fabricID = f.fabricID JOIN payment p ON o.paymentID = p.paymentID WHERE o.status = 'Processing'";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }
}
